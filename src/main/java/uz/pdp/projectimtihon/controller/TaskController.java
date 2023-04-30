package uz.pdp.projectimtihon.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.projectimtihon.entity.Task;
import uz.pdp.projectimtihon.payload.ApiResponse;
import uz.pdp.projectimtihon.payload.PageDTO;
import uz.pdp.projectimtihon.service.TaskService;
import uz.pdp.projectimtihon.utils.AppConstants;

@RestController
@RequestMapping(TaskController.BASE_PATH)
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;
    public static final  String BASE_PATH= AppConstants.BASE_PATH+"/task";
    int size=15;


    @GetMapping("/{projectId}")
    public ApiResponse<PageDTO<Task>> getTask(@RequestParam(defaultValue = "1") int page,@PathVariable Integer projectId){
        taskService.getTasks(page, size,projectId);
      return ApiResponse.successResponse("Ok");
    }


}
