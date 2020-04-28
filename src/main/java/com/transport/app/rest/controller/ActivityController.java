package com.transport.app.rest.controller;

import com.transport.app.rest.domain.Activity;
import com.transport.app.rest.domain.ActivityMessage;
import com.transport.app.rest.domain.User;
import com.transport.app.rest.exception.NotFoundException;
import com.transport.app.rest.mapper.ActivityDto;
import com.transport.app.rest.mapper.ActivityMapper;
import com.transport.app.rest.mapper.ActivityMessageDto;
import com.transport.app.rest.mapper.ActivityMessageMapper;
import com.transport.app.rest.service.ActivityService;
import com.transport.app.rest.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/activity")
public class ActivityController {

    private ActivityService activityService;
    private UserService userService;

    ActivityController(ActivityService activityService, UserService userService) {
        this.activityService = activityService;
        this.userService = userService;
    }

    @PostMapping("/create/{email}")
    public ActivityDto create(@Valid @RequestBody Activity activity, @PathVariable("email") String email) {
        User activityBy = userService.findByEmail(email);
        if (activityBy == null) {
            throw new NotFoundException(User.class, email);
        }
        return ActivityMapper.toActivityDto(activityService.save(activity, activityBy));
    }

    @GetMapping("/get")
    public List<ActivityDto> get() {
        return ActivityMapper.toActivityDtos(activityService.findAll());
    }

    /*ActivityMessage*/

    @PostMapping("/create/activitymessage")
    public ActivityMessageDto createActivityMessage(@Valid @RequestBody ActivityMessage activityMessage) {
        return ActivityMessageMapper.toActivityMessageDto(activityService.saveOrUpdateActivityMessage(activityMessage));
    }

//    @PostMapping("/update/activitymessage")
//    public ActivityMessageDto updateActivityMessage(@Valid @RequestBody ActivityMessage activityMessage) {
//        return ActivityMessageMapper.toActivityMessageDto(activityService.updateActivityMessage(activityMessage));
//    }

    @GetMapping("/get/activitymessage")
    public List<ActivityMessageDto> getActivityMessages() {
        return ActivityMessageMapper.toActivityMessageDtos(activityService.findAllActivityMessages());
    }

    @GetMapping("/get/activitymessage/{type}")
    public ActivityMessageDto getActivityMessageByType(@PathVariable("status") String type) {
        return ActivityMessageMapper.toActivityMessageDto(activityService.findActivityMessageByActivityType(type));
    }
}
