package uz.pdp.projectimtihon.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageDTO<T>{
    private int maxPage;
    private int pageNumber;
    private int size;
    List<T> taskList;
}
