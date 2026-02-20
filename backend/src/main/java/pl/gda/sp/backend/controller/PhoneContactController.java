package pl.gda.sp.backend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.gda.sp.backend.dto.PhoneContactResponse;
import pl.gda.sp.backend.dto.PromptRequest;
import pl.gda.sp.backend.service.PhoneContactService;

import java.util.List;

@RestController
@RequestMapping("/api/contacts")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class PhoneContactController {

    private final PhoneContactService phoneContactService;

    @PostMapping("/command")
    public ResponseEntity<PhoneContactResponse> executeCommand(@RequestBody PromptRequest request) {

        PhoneContactResponse result = phoneContactService.manageContacts(request.prompt());

        if (result == null) {
            return ResponseEntity.ok().body(null);
        }

        return ResponseEntity.ok(result);
    }

    @GetMapping("/all")
    public ResponseEntity<List<PhoneContactResponse>> getAllContacts() {
        return ResponseEntity.ok(phoneContactService.getAllContacts());
    }
}