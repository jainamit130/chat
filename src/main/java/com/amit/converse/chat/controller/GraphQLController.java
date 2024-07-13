package com.amit.converse.chat.resolvers;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import graphql.ExecutionInput;
import graphql.ExecutionResult;
import graphql.GraphQL;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/graphql")
@RequiredArgsConstructor
public class GraphQLController {

    private final QueryResolver queryResolver;
    private final GraphQL graphQL;

    @PostMapping
    public ResponseEntity<Object> graphqlQuery(@RequestBody Map<String, Object> request) {
        String query = (String) request.get("query");
        ExecutionResult result = executeQuery(query);

        Map<String, Object> resultMap = new LinkedHashMap<>();
        if (!result.getErrors().isEmpty()) {
            resultMap.put("errors", result.getErrors());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resultMap);
        }

        resultMap.put("data", result.getData());
        return ResponseEntity.ok(resultMap);
    }

    private ExecutionResult executeQuery(String query) {
        ExecutionInput executionInput = ExecutionInput.newExecutionInput()
                .query(query)
                .build();

        return graphQL.execute(executionInput);
    }
}