package nl.rabobank.api;

import nl.rabobank.RaboAssignmentApplication;
import nl.rabobank.TestUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = RaboAssignmentApplication.class)
@AutoConfigureMockMvc
class AccountServiceITTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testCreateAccountEndpoint() throws Exception {
        this.mockMvc.perform(post("/account/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.asJsonString(TestUtils.createAccountEntity(1))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountHolderName").value("Grantor-1"));
    }

}
