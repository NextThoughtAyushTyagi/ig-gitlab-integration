package com.integrate.pojo.open.ai;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class OpenAiRequest {

       // Model class to represent the JSON structure
        private String model;
        private List<Message> messages;
        private Double temperature=0.2;
        // Inner class to represent each message
        public static class Message {
            private String role;
            private String content;

            // Getters and setters
            public String getRole() {
                return role;
            }
            public void setRole(String role) {
                this.role = role;
            }
            public String getContent() {
                return content;
            }
            public void setContent(String content) {
                this.content = content;
            }
        }
}
