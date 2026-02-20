package pl.gda.sp.backend.mapper;

import pl.gda.sp.backend.domain.PhoneContact;
import pl.gda.sp.backend.dto.PhoneContactResponse;

public class PhoneContactMapper {
    public static PhoneContactResponse toResponse(PhoneContact contact) {
        if (contact == null) {
            return null;
        }
        return PhoneContactResponse.builder()
                .id(contact.getId())
                .name(contact.getName())
                .phoneNumber(contact.getPhoneNumber())
                .build();
    }
}
