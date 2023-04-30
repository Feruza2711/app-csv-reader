package uz.pdp.projectimtihon.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import uz.pdp.projectimtihon.entity.Project;
import uz.pdp.projectimtihon.entity.Section;
import uz.pdp.projectimtihon.entity.Task;
import uz.pdp.projectimtihon.exeptions.RestException;
import uz.pdp.projectimtihon.payload.PageDTO;
import uz.pdp.projectimtihon.payload.SectionDTO;
import uz.pdp.projectimtihon.payload.TableDTO;
import uz.pdp.projectimtihon.repository.ProjectRepository;
import uz.pdp.projectimtihon.repository.SectionRepository;
import uz.pdp.projectimtihon.repository.TaskRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;


    public PageDTO<TableDTO> getTasks(int page, int size,Integer projectId) {
        Optional<Project> byId = projectRepository.findById(projectId);
        if(byId.isEmpty())
            throw RestException.restThrow("Project not found");
        Project project = byId.get();
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Task> task = taskRepository.findAll(pageRequest);
        List<Task> tasks = task.stream().toList();
        SectionDTO sectionDTO=new SectionDTO();
        sectionDTO.setTaskList(tasks);
        Optional<Task> first = task.stream().findFirst();
        if(first.isEmpty()){
            throw  RestException.restThrow("Section not found");
        }
        Task task1 = first.get();
        Section section = task1.getSection();
        sectionDTO.setSectionName(section.getName());

        TableDTO tableDTO=new TableDTO();

        tableDTO.setSectionDTOList(List.of(sectionDTO));

        PageDTO pageDTO=new PageDTO<>();
        pageDTO.setMaxPage(task.getTotalPages());
        pageDTO.setSize(size);
        pageDTO.setTaskList(List.of(tableDTO));
return pageDTO;
    }}
