package com.transport.app.rest.service;

import com.transport.app.rest.Constants;
import com.transport.app.rest.domain.DatabaseFile;
import com.transport.app.rest.domain.Order;
import com.transport.app.rest.domain.OrderStatus;
import com.transport.app.rest.domain.User;
import com.transport.app.rest.exception.NotFoundException;
import com.transport.app.rest.repository.DatabaseFileRepository;
import com.transport.app.rest.repository.DatabaseFileSpecs;
import com.transport.app.rest.repository.OrderRepository;
import com.transport.app.rest.repository.UserSpecs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class DatabaseFileService {

    @Autowired
    private DatabaseFileRepository repository;
    @Autowired
    private OrderRepository orderRepository;

    public DatabaseFile storeFile(MultipartFile file, Long orderId, String location, String dateTime) {
//        Order order = orderRepository.findById(orderId).orElseThrow(() -> new NotFoundException(Order.class, orderId));
        // Normalize file name
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        try {
            String text = orderId + " | " + location + " | " + dateTime;
            DatabaseFile dbFile = new DatabaseFile(fileName, file.getContentType(), drawString(file.getBytes(), text), orderId, location);
            return repository.save(dbFile);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
    public DatabaseFile storeMarkingFile(MultipartFile file, Long orderId, String location, String dateTime) {
//        Order order = orderRepository.findById(orderId).orElseThrow(() -> new NotFoundException(Order.class, orderId));
        // Normalize file name
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        try {
            String text = orderId + " | " + location + " | " + dateTime;
            DatabaseFile dbFile = new DatabaseFile(fileName, file.getContentType(), drawString(file.getBytes(), text), orderId, location, true);
            return repository.save(dbFile);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public DatabaseFile storeSignatureFileAndUpdateOrderStatus(MultipartFile file, Long orderId, String signedBy) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new NotFoundException(User.class, orderId));
        if (Constants.CONSIGNOR.equals(signedBy)) {
            if (repository.getByOrderIdAndSignedBy(orderId, Constants.DRIVER).size() > 0) {
                order.setOrderStatus(OrderStatus.PICKED_UP.getName());
            }
        } else if (Constants.DRIVER.equals(signedBy)) {
            if (repository.getByOrderIdAndSignedBy(orderId, Constants.CONSIGNOR).size() > 0) {
                order.setOrderStatus(OrderStatus.PICKED_UP.getName());
            }
        } else if (Constants.CONSIGNEE.equals(signedBy) &&
                repository.getByOrderIdAndSignedBy(orderId, Constants.CONSIGNOR).size() > 0 &&
                repository.getByOrderIdAndSignedBy(orderId, Constants.DRIVER).size() > 0) {
            order.setOrderStatus(OrderStatus.DELIVERED.getName());
        }
        orderRepository.save(order);
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        DatabaseFile dbFile = null;
        try {
            dbFile = DatabaseFile.builder()
                    .fileName(fileName).orderId(orderId).fileType(file.getContentType()).data(file.getBytes()).signedBy(signedBy).build();
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
        return repository.save(dbFile);
    }

    public DatabaseFile getFile(Long fileId) {
        return repository.findById(fileId).orElseThrow(() -> new RuntimeException("File with id " + fileId + " not found"));
    }

    public List<DatabaseFile> getAllFilesData() {
        return repository.findAll();
    }

    public List<String> getAllFileUriWithFileType(String fileType, String uriPrefix) {
//        return repository.findAll(Specification.where(DatabaseFileSpecs.withFileType(fileType)));
        List<Long> ids = repository.getAllFileUriWithFileType(fileType);
        List<String> uriList = new ArrayList<>();
        for (long id : ids) {
            uriList.add(uriPrefix + id);
        }
        return uriList;
    }

    public List<String> getByOrderIdAndLocation(String fileType, String uriPrefix, Long orderId, String location) {
        List<Long> ids = repository.getByOrderIdAndLocation(fileType, orderId, location);
        List<String> uriList = new ArrayList<>();
        for (long id : ids) {
            uriList.add(uriPrefix + id);
        }
        return uriList;
    }

    public String getByOrderIdAndSignedBy(String uriPrefix, Long orderId, String signedBy) {
        List<Long> ids = repository.getByOrderIdAndSignedBy(orderId, signedBy);
        if (!ids.isEmpty()) {
            return uriPrefix + ids.get(ids.size() - 1);
        }
        return null;
    }

    public List<String> getByOrderIdLocationAndMarking(String uriPrefix, Long orderId, String location) {
        List<Long> ids = repository.getByOrderIdLocationAndMarking(orderId, location);
        List<String> uriList = new ArrayList<>();
        for (long id : ids) {
            uriList.add(uriPrefix + id);
        }
        return uriList;
    }

    private byte[] drawString(byte[] data, String string) throws IOException {
        final BufferedImage image = ImageIO.read(new ByteArrayInputStream(data));
        Graphics g = image.getGraphics();
        g.setFont(g.getFont().deriveFont(30f));
        Color color = g.getColor();
        Color complement = new Color(255 - color.getRed(), 255 - color.getGreen(), 255 - color.getBlue());
        g.setColor(Color.lightGray);
        FontMetrics metrics = g.getFontMetrics();
        int x = (image.getWidth() - metrics.stringWidth(string)) / 2;
        g.drawString(string, x, image.getHeight() - 15);
        g.dispose();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ImageIO.write(image, "png", bos);
        return bos.toByteArray();
    }
}
