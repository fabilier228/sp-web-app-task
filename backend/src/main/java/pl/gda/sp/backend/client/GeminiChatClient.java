package pl.gda.sp.backend.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import pl.gda.sp.backend.dto.GeminiChatResponse;

@Component
@RequiredArgsConstructor
public class GeminiChatClient {


    @Value("${spring.ai.google.genai.api-key}")
    private String apiKey;

    @Value("${spring.ai.google.genai.chat.options.model}")
    private String model;
    private final ObjectMapper objectMapper = new ObjectMapper();

    private Client client;

    @PostConstruct
    public void initClient() {
        this.client = Client.builder().apiKey(apiKey).build();
    }

    public GeminiChatResponse sendMessage(String context) {
        try {
            String fullPrompt = createPrompt(context);

            GenerateContentResponse response = client.models.generateContent(
                    this.model,
                    fullPrompt,
                    null
            );

            String jsonText = response.text();

            if (jsonText != null && jsonText.startsWith("```json")) {
                jsonText = jsonText.replace("```json", "").replace("```", "").trim();
            }

            return objectMapper.readValue(jsonText, GeminiChatResponse.class);

        } catch (Exception e) {
            throw new RuntimeException("Error while communicating with API", e);
        }
    }

    public String createPrompt(String context) {
        return """
                You are a helpful assistant that provides and manages information about phone contacts.
                Following request contains at least one of key words: name or phone number.
                Request context: %s
                
                Based on the request, determine the appropriate action to perform on the phone contact information.
                The possible actions are: ADD_CONTACT, DELETE_CONTACT, GET_CONTACT, UPDATE_CONTACT, NONE.
                
                Provide the response strictly in the following JSON format without any Markdown formatting:
                {   
                    "response": "RESPONSE_MESSAGE",
                    "method": "ACTION_NAME",
                    "name": "CONTACT_NAME",
                    "phoneNumber": "CONTACT_PHONE_NUMBER"
                }
                
                If the request does not contain enough information to perform any of the actions, respond with method: NONE and null for name and phone number.
                
                Example answer with provided name and phone number:
                {
                    "response": "Contact added successfully.",
                    "method": "ADD_CONTACT",
                    "name": "John Doe",
                    "phoneNumber": "123-456-7890"
                }
                
                Example answer with provided name only:
                {
                    "response": lack of phone number and method information.",
                    "method": "NONE",
                    "name": "John Doe",
                    "phoneNumber": null
                }
                """.formatted(context);
    }
}