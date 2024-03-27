package com.spring.boot.controller;

import com.spring.boot.TestResources;
import com.spring.boot.services.ContactManagerService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(printOnlyOnFailure = false)
@Transactional
public class ContactControllerIT {

    @Autowired
    private ContactManagerService contactManagerService;

    @Autowired
    private MockMvc mockMvc;

    @Nested
    public class TestCasesForJoe {

        @Test
        @DisplayName("GET /api/contacts -> 200 OK")
        public void should_respond_with_all_the_contacts() throws Exception {
            mockMvc.perform(get("/api/contacts")
                .accept(MediaType.ALL)
                .header("Authorization", "Bearer "+TestResources.jwtTokenForJoe())
            )
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$[*].name").value(containsInAnyOrder("Greg from accounting", "Coworker Fred", "Sister Monica")))
            .andExpect(jsonPath("$[*].phoneNumbers.*").value(hasSize(5)))
            .andExpect(jsonPath("$[*].addresses.*").value(hasSize(4)))
            .andExpect(jsonPath("$[*].emails.*").value(hasSize(3)));
        }

        @Test
        @DisplayName("GET /api/contacts/5c21433c-3c70-4253-a4b2-52b157be4167 --> 200 OK")
        public void should_respond_with_a_contact_successfully() throws Exception {
            mockMvc.perform(get("/api/contacts/"+UUID.fromString("5c21433c-3c70-4253-a4b2-52b157be4167"))
                .header("Authorization", "Bearer "+TestResources.jwtTokenForJoe())
                .accept(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.name").value("Greg from accounting"))
            .andExpect(jsonPath("$.phoneNumbers.*").value(hasSize(1)))
            .andExpect(jsonPath("$.emails.*").value(hasSize(1)))
            .andExpect(jsonPath("$.addresses.*").value(hasSize(2)));
        }

        @Test
        @DisplayName("POST /api/contacts -> 201 CREATED")
        void should_create_a_new_contact_successfully() throws Exception {
            final String jsonContent = """
            {
                "name": "Steve",
                "phoneNumbers": {
                    "personal": "+817283640198"
                },
                "emails": {
                    "main": "stevan@mymail.com"
                },
                "addresses": {
                    "home": {
                        "country": "United States",
                        "street": "467 Jennifer Lane",
                        "state": "North Carolina",
                        "city": "Cary",
                        "zipcode": "27513"
                    }
                }
            }
            """;

            mockMvc.perform(post("/api/contacts")
                .header("Authorization", "Bearer " + TestResources.jwtTokenForJoe())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonContent)
            )
            .andExpect(status().isCreated());
        }

        @Test
        @DisplayName("PUT /api/contacts/4fe25947-ecab-489c-a881-e0057124e408 -> 200 OK")
        void should_update_isolated_fields_of_a_contact_successfully() throws Exception {
            final String requestBody = """
            {
                "name": "Bill",
                "phoneNumbers": {
                    "cellphone": "+811234567890"
                },
                "addresses": {
                }
            }
            """;

            mockMvc.perform(put("/api/contacts/"+UUID.fromString("4fe25947-ecab-489c-a881-e0057124e408"))
                .header("Authorization", "Bearer "+TestResources.jwtTokenForJoe())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
            )
            .andExpect(status().isOk());
        }

        @Test
        @DisplayName("DELETE /api/contacts/5c21433c-3c70-4253-a4b2-52b157be4167 -> 200 OK")
        public void should_delete_a_contact_successfully() throws Exception {
            mockMvc.perform(delete("/api/contacts/"+UUID.fromString("5c21433c-3c70-4253-a4b2-52b157be4167"))
                .header("Authorization", "Bearer "+TestResources.jwtTokenForJoe())
                .accept(MediaType.ALL)
            )
            .andExpect(status().isOk());
        }
    }

    @Nested
    public class TestCasesForRobert {

        @Test
        @DisplayName("GET /api/contacts -> 200 OK")
        public void should_respond_with_all_the_contacts() throws Exception {
            mockMvc.perform(get("/api/contacts")
                .accept(MediaType.ALL)
                .header("Authorization", "Bearer "+TestResources.jwtTokenForRobert())
            )
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$[*].name").value(containsInAnyOrder("Best friend Julia", "Mom", "Pizza and burgers", "Uncle Jeff")))
            .andExpect(jsonPath("$[*].phoneNumbers.*").value(hasSize(7)))
            .andExpect(jsonPath("$[*].addresses.*").value(hasSize(7)))
            .andExpect(jsonPath("$[*].emails.*").value(hasSize(7)));
        }

        @Test
        @DisplayName("GET /api/contacts/b621650d-4a81-4016-a917-4a8a4992aaef --> 200 OK")
        public void should_respond_with_a_contact_successfully() throws Exception {
            mockMvc.perform(get("/api/contacts/"+UUID.fromString("b621650d-4a81-4016-a917-4a8a4992aaef"))
                .header("Authorization", "Bearer "+TestResources.jwtTokenForRobert())
                .accept(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.name").value("Uncle Jeff"))
            .andExpect(jsonPath("$.phoneNumbers.*").value(hasSize(2)))
            .andExpect(jsonPath("$.emails.*").value(hasSize(1)))
            .andExpect(jsonPath("$.addresses.*").value(hasSize(2)));
        }

        @Test
        @DisplayName("POST /api/contacts -> 201 CREATED")
        void should_create_a_new_contact_successfully() throws Exception {
            final String jsonContent = """
            {
                "name": "Steve",
                "phoneNumbers": {
                    "personal": "+817283640198"
                },
                "emails": {
                    "main": "stevan@mymail.com"
                },
                "addresses": {
                    "home": {
                        "country": "United States",
                        "street": "467 Jennifer Lane",
                        "state": "North Carolina",
                        "city": "Cary",
                        "zipcode": "27513"
                    }
                }
            }
            """;

            mockMvc.perform(post("/api/contacts")
                .header("Authorization", "Bearer " + TestResources.jwtTokenForRobert())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonContent)
            )
            .andExpect(status().isCreated());
        }

        @Test
        @DisplayName("PUT /api/contacts/84edd1b9-89a5-4107-a84d-435676c2b8f5 -> 200 OK")
        void should_update_isolated_fields_of_a_contact_successfully() throws Exception {
            final String requestBody = """
            {
                "name": "Bill",
                "phoneNumbers": {
                    "cellphone": "+811234567890"
                },
                "addresses": {
                }
            }
            """;

            mockMvc.perform(put("/api/contacts/"+UUID.fromString("84edd1b9-89a5-4107-a84d-435676c2b8f5"))
                .header("Authorization", "Bearer "+TestResources.jwtTokenForRobert())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
            )
            .andExpect(status().isOk());
        }

        @Test
        @DisplayName("DELETE /api/contacts/b621650d-4a81-4016-a917-4a8a4992aaef -> 200 OK")
        public void should_delete_a_contact_successfully() throws Exception {
            mockMvc.perform(delete("/api/contacts/"+UUID.fromString("b621650d-4a81-4016-a917-4a8a4992aaef"))
                .header("Authorization", "Bearer "+TestResources.jwtTokenForRobert())
                .accept(MediaType.ALL)
            )
            .andExpect(status().isOk());
        }
    }

    @Test
    @DisplayName("GET /api/contacts/c97775aa-b7f3-49c0-a586-d0466ba592bf -> 404 NOT FOUND")
    void should_respond_404_NOT_FOUND_when_requesting_for_a_contact_that_does_not_exist() throws Exception {
        mockMvc.perform(get("/api/contacts/"+UUID.fromString("c97775aa-b7f3-49c0-a586-d0466ba592bf"))
            .header("Authorization", "Bearer "+TestResources.jwtTokenForJoe())
            .accept(MediaType.ALL)
        )
        .andExpect(status().isNotFound())
        .andExpect(content().contentType(MediaType.TEXT_PLAIN))
        .andExpect(content().string("Contact not found"));
    }

    @Test
    @DisplayName("GET /api/contacts/4fe25947-ecab-489c-a881-e0057124e408 -> 400 BAD REQUEST")
    void should_respond_400_when_requesting_for_a_contact_that_does_not_belong_to_the_current_user() throws Exception {
        mockMvc.perform(get("/api/contacts/"+UUID.fromString("4fe25947-ecab-489c-a881-e0057124e408"))
            .header("Authorization", "Bearer "+TestResources.jwtTokenForRobert())
            .accept(MediaType.ALL)
        )
        .andExpect(status().isBadRequest())
        .andExpect(content().contentType(MediaType.TEXT_PLAIN))
        .andExpect(content().string("Contact does not belong to the user: robert"));
    }

    @Test
    @DisplayName("POST /api/contacts -> 400 BAD_REQUEST")
    void should_respond_400_with_field_violation_errors_when_posting_a_bad_formatted_json() throws Exception {
        final String jsonContent = """
        {
            "phoneNumbers": {
            },
            "emails": {
            }
        }
        """;

        mockMvc.perform(post("/api/contacts")
            .header("Authorization", "Bearer "+TestResources.jwtTokenForRobert())
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content(jsonContent)
        )
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.fieldViolations").value(hasSize(3)))
        .andExpect(jsonPath("$.fieldViolations[*]").value(containsInAnyOrder(
            "'phoneNumbers' must have at least 1 item",
            "'name' must not be null",
            "'phoneNumbers' must have between 1 and 50 items"
        )));
    }

    @Test
    @DisplayName("PUT /api/contacts/b621650d-4a81-4016-a917-4a8a4992aaef -> 400 BAD REQUEST")
    void should_respond_400_when_trying_to_update_a_concat_that_does_not_belong_to_the_current_user() throws Exception {
        mockMvc.perform(put("/api/contacts/"+UUID.fromString("b621650d-4a81-4016-a917-4a8a4992aaef"))
            .header("Authorization", "Bearer "+TestResources.jwtTokenForJoe())
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"name\": \"Billy\"}")
            .accept(MediaType.ALL)
        )
        .andExpect(status().isBadRequest())
        .andExpect(content().contentType(MediaType.TEXT_PLAIN))
        .andExpect(content().string("Contact does not belong to the user: joe"));
    }

    @Test
    @DisplayName("DELETE /api/contacts/8fb2bd75-9aec-4cc5-b77b-a95f06081388 -> 400 BAD_REQUEST")
    public void should_respond_400_when_deleting_a_concat_whose_user_does_not_own_it() throws Exception {
        mockMvc.perform(delete("/api/contacts/"+UUID.fromString("8fb2bd75-9aec-4cc5-b77b-a95f06081388"))
            .header("Authorization", "Bearer "+TestResources.jwtTokenForJoe())
            .accept(MediaType.ALL)
        )
        .andExpect(status().isBadRequest())
        .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN))
        .andExpect(content().string("Contact does not belong to the user: joe"));
    }
}
