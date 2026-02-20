package pl.gda.sp.backend.service;

import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.gda.sp.backend.client.GeminiChatClient;
import pl.gda.sp.backend.domain.PhoneContact;
import pl.gda.sp.backend.dto.PhoneContactResponse;
import pl.gda.sp.backend.exception.BusinessRuleException;
import pl.gda.sp.backend.exception.ResourceNotFoundException;
import pl.gda.sp.backend.mapper.PhoneContactMapper;
import pl.gda.sp.backend.repository.PhoneContactRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PhoneContactService {

    private final PhoneContactRepository phoneContactRepository;
    private final GeminiChatClient geminiChatClient;

    @Transactional
    public PhoneContactResponse manageContacts(String prompt) {
        val response = geminiChatClient.sendMessage(prompt);

        if (response.getMethod() == null) {
            throw new ResourceNotFoundException("Invalid method returned by AI");
        }

        return switch (response.getMethod()) {
            case ADD_CONTACT -> addContact(response.getName(), response.getPhoneNumber(), response.getResponse());
            case DELETE_CONTACT -> deleteContact(response.getName(), response.getPhoneNumber(), response.getResponse());
            case UPDATE_CONTACT -> updateContact(response.getName(), response.getPhoneNumber(), response.getResponse());
            case GET_CONTACT -> getPhoneContact(response.getName());
            case NONE -> throw new BusinessRuleException("AI could not determine the action from the prompt");
            default -> throw new ResourceNotFoundException("Invalid method");
        };
    }

    public List<PhoneContactResponse> getAllContacts() {
        val contacts = phoneContactRepository.findAll();
        return contacts.stream()
                .map(PhoneContactMapper::toResponse)
                .toList();
    }

    private PhoneContactResponse addContact(String name, String phoneNumber, String chatResponse) {
        if (name == null || phoneNumber == null) {
            throw new BusinessRuleException("Name and phone number are required to add a contact");
        }
        val contact = PhoneContact.builder()
                .name(name)
                .phoneNumber(phoneNumber)
                .build();

        val savedContact = phoneContactRepository.save(contact);
        val response = PhoneContactMapper.toResponse(savedContact);
        response.setResponse(chatResponse);
        return response;
    }

    private PhoneContactResponse deleteContact(String name, String phoneNumber,  String chatResponse) {
        if (name == null && phoneNumber == null) {
            throw new BusinessRuleException("Either name or phone number is required to delete a contact");
        }

        val contact = phoneContactRepository.findByNameOrPhoneNumber(name, phoneNumber);
        if (contact == null) {
            throw new ResourceNotFoundException("Contact not found");
        }

        val response = PhoneContactMapper.toResponse(contact);
        response.setResponse(chatResponse);
        phoneContactRepository.delete(contact);
        return response;
    }

    private PhoneContactResponse updateContact(String name, String phoneNumber,  String chatResponse) {
        if (name == null || phoneNumber == null) {
            throw new BusinessRuleException("Both name and new phone number are required to update");
        }

        val contact = phoneContactRepository.findByName(name);
        if (contact == null) {
            throw new ResourceNotFoundException("Contact with name " + name + " not found");
        }

        contact.setPhoneNumber(phoneNumber);
        val updatedContact = phoneContactRepository.save(contact);
        val response = PhoneContactMapper.toResponse(updatedContact);
        response.setResponse(chatResponse);

        return response;
    }

    private PhoneContactResponse getPhoneContact(String name) {
        if (name == null) {
            throw new BusinessRuleException("Name is required to get a contact");
        }

        val contact = phoneContactRepository.findByName(name);
        if (contact == null) {
            throw new ResourceNotFoundException("Contact not found");
        }

        return PhoneContactMapper.toResponse(contact);
    }
}