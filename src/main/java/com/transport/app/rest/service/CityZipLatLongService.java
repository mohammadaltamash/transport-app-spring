package com.transport.app.rest.service;

import com.transport.app.rest.domain.CityZipLatLong;
import com.transport.app.rest.repository.CityZipLatLongRepository;
import de.siegmar.fastcsv.reader.CsvParser;
import de.siegmar.fastcsv.reader.CsvReader;
import de.siegmar.fastcsv.reader.CsvRow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
public class CityZipLatLongService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private CityZipLatLongRepository repository;

    public List<CityZipLatLong> findByZipOrCityLike(String text) {
        return repository.findByZipContainsOrCityContainsIgnoreCase(text, text);
    }

    public Integer add() {
        int count = 0;
//        if (repository.count() != 43191) {
            File file = new File("src/main/resources/static/us-zip-code-latitude-and-longitude.csv");
            CsvReader csvReader = new CsvReader();
            csvReader.setContainsHeader(true);

            List<CityZipLatLong> list = new ArrayList<>();
            try (CsvParser csvParser = csvReader.parse(file, StandardCharsets.UTF_8)) {
                CsvRow row;
                while ((row = csvParser.nextRow()) != null) {
                    System.out.println("Read line: " + row);
                    System.out.println("First column of line: " + row.getField(0));

                    list.add(CityZipLatLong.builder()
                            .zip(row.getField(0))
                            .city(row.getField(1))
                            .state(row.getField(2))
                            .latitude(Double.parseDouble(row.getField(3)))
                            .longitude(Double.parseDouble(row.getField(4)))
                            .timezone(Integer.parseInt(row.getField(5)))
                            .geopoint(row.getField(7)).build());
                }
            } catch (IOException e) {
                logger.warn(e.getMessage());
            }
            count = repository.saveAll(list).size();
//        }
        return count;
    }
}
