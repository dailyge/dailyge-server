package project.dailyge.app.test.note.documentationtest.snippet

import org.springframework.restdocs.payload.FieldDescriptor
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.payload.PayloadDocumentation.requestFields
import org.springframework.restdocs.payload.PayloadDocumentation.responseFields
import org.springframework.restdocs.payload.RequestFieldsSnippet
import org.springframework.restdocs.payload.ResponseFieldsSnippet
import project.dailyge.app.common.SnippetUtils.getAttribute
import project.dailyge.app.core.note.presentation.request.NoteCreateRequest

interface NoteSnippet {
    companion object {
        const val TAG = "Notes"

        val NOTE_CREATE_REQUEST_FIELDS: List<FieldDescriptor> = listOf(
            fieldWithPath("title").description("제목")
                .attributes(getAttribute(NoteCreateRequest::class.java, "title")),
            fieldWithPath("content").description("내용")
                .attributes(getAttribute(NoteCreateRequest::class.java, "content")),
            fieldWithPath("nickname").description("닉네임")
                .attributes(getAttribute(NoteCreateRequest::class.java, "title")),
            fieldWithPath("sendAt").description("전송 날짜")
                .attributes(getAttribute(NoteCreateRequest::class.java, "sendAt"))
        )

        val NOTE_CREATE_RESPONSE_FIELDS: List<FieldDescriptor> = listOf(
            fieldWithPath("data.noteId").type(JsonFieldType.NUMBER).description("Note ID"),
            fieldWithPath("code").type(JsonFieldType.NUMBER).description("응답 코드"),
            fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지")
        )

        val NOTE_CREATE_REQUEST_SNIPPET: RequestFieldsSnippet = requestFields(NOTE_CREATE_REQUEST_FIELDS)
        val NOTE_CREATE_RESPONSE_SNIPPET: ResponseFieldsSnippet = responseFields(NOTE_CREATE_RESPONSE_FIELDS)
    }
}
