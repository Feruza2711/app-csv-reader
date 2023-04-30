package uz.pdp.projectimtihon.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import uz.pdp.projectimtihon.entity.Project;
import uz.pdp.projectimtihon.payload.ApiResponse;
import uz.pdp.projectimtihon.service.ProjectService;
import uz.pdp.projectimtihon.utils.AppConstants;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(ProjectController.BASE_PATH)
public class ProjectController {
    private final ProjectService projectService;
    public static final String BASE_PATH = AppConstants.BASE_PATH + "/project";

    @PutMapping("/{id}")
    public ApiResponse<String> editProjectName(@RequestBody String name, @PathVariable Integer id){
        return projectService.editProjectName(name,id);
    }

    @GetMapping("/{userId}")
    public ApiResponse<List<Project>> getAllProjects(@PathVariable Integer userId){
        return projectService.getAllProjects(userId);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<String> deleteProject(@PathVariable Integer id){
        return projectService.deleteProject(id);
    }
}
