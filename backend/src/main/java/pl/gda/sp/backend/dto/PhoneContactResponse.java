package pl.gda.sp.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PhoneContactResponse {
    private Long id;
    private String response;
    private String name;
    private String phoneNumber;
}
