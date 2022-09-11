package ir.mojir.document_manager.rest_controllers;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.partWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParts;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.snippet.Snippet;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;

import ir.mojir.document_manager.dtos.FileSearchFilters;
import ir.mojir.spring_boot_commons.dtos.Paging;
import ir.mojir.spring_boot_commons.dtos.SearchDto;
import ir.mojir.spring_boot_commons.dtos.Sorting;

@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@SpringBootTest
public class DocumentControllerTest {

	private MockMvc mockMvc;
	
	
	@Test
	public void searchTest() throws Exception {
		SearchDto<FileSearchFilters> req = new SearchDto<>();
		FileSearchFilters filters = new FileSearchFilters();
		req.setFilters(filters);
		req.setPaging(new Paging(0, 5));
		req.setSorting(new Sorting(true, "fileName"));
		mockMvc.perform(
				post("/search")
				.content(new ObjectMapper().writeValueAsString(req))
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andDo(document("search",
					documentSearchReqFields(), 
					documentSearchResponseFields()
				));
	}
	
//	@Test
//	public void getTest() throws Exception {
//		mockMvc.perform(RestDocumentationRequestBuilders.get("/documents/{documentId}", "1"))
//			.andExpect(status().isOk())
//			.andDo(document("get", pathParameters(
//					parameterWithName("documentId").description("The ID of the doument")
//		)));
//	}
	
	@Test
	public void multipartUploadTest() throws Exception {
		mockMvc.perform(multipart("/upload").file("file", "fileContent".getBytes()))
			.andExpect(status().isOk())
			.andDo(document("upload-multipart", requestParts(
					partWithName("file").description("The file to upload")
		)));
	}
	
	private Snippet documentSearchResponseFields() {
		return responseFields(
				fieldWithPath("[]").description("An array of documents")					
			).andWithPrefix("[].", documentDocument());
	}
	
	private FieldDescriptor[] documentDocument() {
		return new FieldDescriptor[] {
			fieldWithPath("id").description("id of document"),
			fieldWithPath("fileName").description("file name"),
			fieldWithPath("fileExtension").description("file extension")
		};
	}

	private Snippet documentSearchReqFields() {
		return requestFields(
			fieldWithPath("filters.fileName").description("part of file name to filter by"),
			fieldWithPath("paging.pageNumber").description("page number starting from zero"),
			fieldWithPath("paging.pageSize").description("page size"),
			fieldWithPath("sorting.ascending").description("'true' for ascending and 'false' for descending sort"),
			fieldWithPath("sorting.sortField").description("sort field or column")
		);
	}

	@BeforeEach
	public void setUp(WebApplicationContext webApplicationContext,
			RestDocumentationContextProvider restDocumentation) {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
				.apply(documentationConfiguration(restDocumentation)
						.operationPreprocessors()
							.withRequestDefaults(prettyPrint())
							.withResponseDefaults(prettyPrint())
				)
				.build();
	}
	
}
