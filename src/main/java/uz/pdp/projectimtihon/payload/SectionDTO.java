package uz.pdp.projectimtihon.payload;

import lombok.Data;
import uz.pdp.projectimtihon.entity.Section;
import uz.pdp.projectimtihon.entity.Task;

import java.util.List;
@Data
public class SectionDTO {
    private String sectionName;
    private List<Task> taskList;
}
