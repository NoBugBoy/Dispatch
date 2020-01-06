package beans;

import lombok.Data;

import java.util.List;

@Data
public class BaseRequest {
    private List<String> label;
    private Boolean avgDispatch;
    private  String msg;
}
