package org.ow2.proactive.scheduling.api.controllers;


import java.util.Map;

import org.ow2.proactive.scheduling.api.services.AuthenticationService;
import org.ow2.proactive.scheduling.api.services.GraphqlService;
import com.google.common.collect.ImmutableMap;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@WebAppConfiguration
public class GraphQLControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Mock
    private AuthenticationService authenticationService;

    @Mock
    private GraphqlService graphqlService;

    @InjectMocks
    private GraphQLController graphQLController;

    private MockMvc mockMvc;

    private String query = "{ \"query\": \"{ jobs{ edges{ node{ id } } } } \" }";

    @Before
    public void setup() {
        initMocks(this);

        when(authenticationService.authenticate(any(String.class))).thenReturn("bobot");

        when(graphqlService.executeQuery(any(String.class), any(String.class),
                any(GraphqlService.GraphqlContext.class), any(Map.class))).thenReturn(ImmutableMap.of());


        mockMvc = standaloneSetup(graphQLController).build();
    }

    @Test
    public void testControllerPostMethod() throws Exception {

        mockMvc.perform(post("/v2/graphql").header("sessionid", "sessionId").accept(
                MediaType.APPLICATION_JSON).contentType(
                MediaType.APPLICATION_JSON).content(query)).andExpect(status().isOk()).andExpect(
                content().contentType(MediaType.APPLICATION_JSON));

        verify(authenticationService, times(1)).authenticate(any(String.class));

        verify(graphqlService, times(1)).executeQuery(any(String.class), any(String.class),
                any(GraphqlService.GraphqlContext.class), any(Map.class));
    }

    @Test
    public void testControllerGetMethod() throws Exception {

        mockMvc.perform(
                get("/v2/graphql").header("sessionid", "sessionId").param("query",
                        "{ jobs{ edges{ node{ id } } } }").accept(
                        MediaType.APPLICATION_JSON).contentType(
                        MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andExpect(
                content().contentType(MediaType.APPLICATION_JSON));

        verify(authenticationService, times(1)).authenticate(any(String.class));

        verify(graphqlService, times(1)).executeQuery(any(String.class), any(String.class),
                any(GraphqlService.GraphqlContext.class), any(Map.class));
    }

}