/*
 * Copyright 2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package MailService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class EmailControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void validEmailShouldReturnOK() throws Exception {

        this.mockMvc.perform(post("/sendEmail").contentType(MediaType.APPLICATION_JSON)
                .content("{\"to\":[\"roeyaus@gmail.com\"], \"cc\": [\"roeyaus@gmail.com\"], \"bcc\" : [\"roeyaus@gmail.com\"], \"subject\" : \"1121\", \"from\":\"me@me.com\", \"text\" : \"blablabla\"}")).andDo(print()).andExpect(status().isOk());

    }

    @Test
    public void invalidEmailAddressShouldReturnBadRequest() throws Exception {

        //THIS SHOULD BE VALIDATED IN THE APPLICATION - BUT IT'S NOT.

        this.mockMvc.perform(post("/sendEmail").contentType(MediaType.APPLICATION_JSON)
                .content("{\"to\":[\"roeyaus@gmom\"], \"cc\": [\"roeyaus@gmail.com\"], \"bcc\" : [\"roeyaus@gmail.com\"], \"subject\" : \"1121\", \"text\" : \"blablabla\"}")).andDo(print()).andExpect(status().isBadRequest());
    }

    @Test
    public void EmailWithoutFromShouldReturnBadRequest() throws Exception {

        this.mockMvc.perform(post("/sendEmail").contentType(MediaType.APPLICATION_JSON)
                .content("{\"to\":[\"roeyaus@gmail.com\"], \"cc\": [\"roeyaus@gmail.com\"], \"bcc\" : [\"roeyaus@gmail.com\"], \"subject\" : \"1121\", \"text\" : \"blablabla\"}")).andDo(print()).andExpect(status().isBadRequest());

    }

    @Test
    public void EmailWithoutToShouldReturnBadRequest() throws Exception {

        this.mockMvc.perform(post("/sendEmail").contentType(MediaType.APPLICATION_JSON)
                .content("{\"cc\": [\"roeyaus@gmail.com\"], \"bcc\" : [\"roeyaus@gmail.com\"], \"subject\" : \"1121\", \"from\":\"me@me.com\", \"text\" : \"blablabla\"}")).andDo(print()).andExpect(status().isBadRequest());

    }

    @Test
    public void EmailWithMultipleRecepientsShouldReturnOK() throws Exception {

        this.mockMvc.perform(post("/sendEmail").contentType(MediaType.APPLICATION_JSON)
                .content("{\"to\":[\"roeyaus@gmail.com\", \"roey.lehman@eyesight-tech.com\"], \"cc\": [\"roeyaus@gmail.com\"], \"bcc\" : [\"roeyaus@gmail.com\"], \"subject\" : \"1121\", \"from\":\"me@me.com\", \"text\" : \"blablabla\"}")).andDo(print()).andExpect(status().isOk());

    }

    @Test
    public void EmailWithoutCCReturnsOK() throws Exception {

        this.mockMvc.perform(post("/sendEmail").contentType(MediaType.APPLICATION_JSON)
                .content("{\"to\":[\"roeyaus@gmail.com\"], \"bcc\" : [\"roeyaus@gmail.com\"], \"subject\" : \"1121\", \"from\":\"me@me.com\", \"text\" : \"blablabla\"}")).andDo(print()).andExpect(status().isOk());

    }

    @Test
    public void EmailWithoutBCCReturnsOK() throws Exception {

        this.mockMvc.perform(post("/sendEmail").contentType(MediaType.APPLICATION_JSON)
                .content("{\"to\":[\"roeyaus@gmail.com\"], \"cc\" : [\"roeyaus@gmail.com\"], \"subject\" : \"1121\", \"from\":\"me@me.com\", \"text\" : \"blablabla\"}")).andDo(print()).andExpect(status().isOk());
    }

    @Test
    public void EmailWithoutSubjectReturnsOK() throws Exception {

        this.mockMvc.perform(post("/sendEmail").contentType(MediaType.APPLICATION_JSON)
                .content("{\"to\":[\"roeyaus@gmail.com\"], \"cc\" : [\"roeyaus@gmail.com\"], \"bcc\" : [\"roeyaus@gmail.com\"], \"from\":\"me@me.com\", \"text\" : \"blablabla\"}")).andDo(print()).andExpect(status().isOk());
    }



}
