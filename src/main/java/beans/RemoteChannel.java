package beans;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RemoteChannel {

    /**
     * channel所在的服务器
     */
    private String ip;
    private List<String> label;
}
