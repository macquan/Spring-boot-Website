package PMQ.local.SpringBootProject.controllers;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor; // ArgumentCaptor là một lớp trong thư viện Mockito được sử dụng để "bắt" các đối số (arguments) được truyền vào các phương thức của mock object trong quá trình kiểm thử. Nó cho phép bạn kiểm tra và xác nhận các giá trị của các đối số mà một phương thức đã nhận, giúp đảm bảo rằng các phương thức được gọi với các tham số đúng như mong đợi.
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import PMQ.local.SpringBootProject.mappers.BaseMapper;
import PMQ.local.SpringBootProject.modules.users.services.interfaces.BaseServiceInterface;

public abstract class BaseControllerTest<E, R, C, U, Repo extends JpaRepository<E, Long> & JpaSpecificationExecutor<E>, M extends BaseMapper<E, R, C, U>, S extends BaseServiceInterface<E, C, U>> {

    @Autowired
    protected MockMvc mockMvc; // MockMvc là một đối tượng được sử dụng để mô phỏng các yêu cầu HTTP và kiểm
                               // tra các phản hồi từ các controller trong ứng dụng Spring Boot. Nó cho phép
                               // bạn thực hiện các kiểm thử tích hợp (integration tests) mà không cần phải
                               // chạy toàn bộ ứng dụng.

    @MockitoBean // Annotation này được sử dụng để tạo ra một bean giả lập (mock bean) trong môi
                 // trường kiểm thử. Nó cho phép bạn thay thế các bean thực tế bằng các phiên bản
                 // giả lập để kiểm tra hành vi của controller mà không cần phải phụ thuộc vào
                 // các dịch vụ thực tế.
    protected S service;

    @MockitoBean
    protected M mapper;

    protected abstract String getApiPath();

    protected abstract String getTestKeyword();

    protected abstract Map<String, String[]> getTestSimpleFilter();

    protected abstract ResultActions getExpectResponseData(ResultActions result, List<R> resource)
            throws Exception;

    protected abstract ResultActions getExpectResponseFilterData(ResultActions result, List<R> resource)
            throws Exception;

    protected abstract List<E> createTestEntities();

    protected abstract List<E> createTestEntitiesByKeywordFiltered(List<E> entities, String keyword);

    protected abstract List<E> createTestEntitiesBySimpleFiltered(List<E> entities, Map<String, String[]> filters);

    protected abstract List<R> createTestResources();

    protected abstract List<R> createTestResourcesByKeywordFiltered(List<R> resources, String keyword);

    protected abstract List<R> createTestResourcesBySimpleFiltered(List<R> resources, Map<String, String[]> filters);

    /* Test trường hợp lấy dữ liệu không có filter -> lấy toàn bộ */
    @Test
    void list_NoFilter_ShouldReturnAllRecords() throws Exception {
        List<E> mockEntities = createTestEntities();
        List<R> mockResources = createTestResources();

        @SuppressWarnings("unchecked")
        ArgumentCaptor<Map<String, String[]>> captor = ArgumentCaptor.forClass(Map.class);

        when(service.getAll(captor.capture())).thenReturn(mockEntities);
        when(mapper.toList(mockEntities)).thenReturn(mockResources);

        mockMvc.perform(get(getApiPath() + "/list")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Entities retrieved successfully"))
                .andExpect(jsonPath("$.status").value("200 OK"))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.errors").doesNotExist())
                .andExpect(jsonPath("$.error").doesNotExist());

        verify(service).getAll(captor.getValue());
        verify(mapper).toList(mockEntities);

    }

    /* Test trường hợp lấy dữ liệu có filter -> keyword */
    @Test
    void list_WithKeywordFilter_ShouldReturnFilteredKeywordRecords() throws Exception {
        List<E> mockEntities = createTestEntities();
        List<R> mockResources = createTestResources();
        List<E> mockFilteredEntities = createTestEntitiesByKeywordFiltered(mockEntities, getTestKeyword());
        List<R> mockFilteredResources = createTestResourcesByKeywordFiltered(mockResources, getTestKeyword());

        @SuppressWarnings("unchecked")
        ArgumentCaptor<Map<String, String[]>> captor = ArgumentCaptor.forClass(Map.class);

        when(service.getAll(captor.capture())).thenReturn(mockFilteredEntities);
        when(mapper.toList(mockFilteredEntities)).thenReturn(mockFilteredResources);

        ResultActions actions = mockMvc.perform(get(getApiPath() + "/list")
                .param("keyword", getTestKeyword())
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print());

        getExpectResponseData(actions, mockFilteredResources)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Entities retrieved successfully"))
                .andExpect(jsonPath("$.status").value("200 OK"))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.errors").doesNotExist())
                .andExpect(jsonPath("$.error").doesNotExist());

        verify(service).getAll(captor.getValue());
        verify(mapper).toList(mockFilteredEntities);

        Map<String, String[]> capturedParams = captor.getValue();
        assertThat(capturedParams.get("keyword")).containsExactly(getTestKeyword());

    }

    /* Test trường hợp lấy dữ liệu với filter -> filterSimple dạng where = */
    @Test
    void list_WithSimpleFilter_ShouldReturnSimpleFilteredRecords() throws Exception {
        Map<String, String[]> filters = getTestSimpleFilter();
        List<E> mockEntities = createTestEntities();
        List<R> mockResources = createTestResources();
        List<E> mockFilteredEntities = createTestEntitiesBySimpleFiltered(mockEntities, filters);
        List<R> mockFilteredResources = createTestResourcesBySimpleFiltered(mockResources, filters);

        @SuppressWarnings("unchecked")
        ArgumentCaptor<Map<String, String[]>> captor = ArgumentCaptor.forClass(Map.class);

        when(service.getAll(captor.capture())).thenReturn(mockFilteredEntities);
        when(mapper.toList(mockFilteredEntities)).thenReturn(mockFilteredResources);

        MockHttpServletRequestBuilder requestBuilder = get(getApiPath() + "/list");
        for (Map.Entry<String, String[]> entry : filters.entrySet()) {
            String key = entry.getKey();
            String[] values = entry.getValue();
            for (String value : values) {
                requestBuilder.param(key, value);
            }
        }

        ResultActions actions = mockMvc.perform(requestBuilder.contentType(MediaType.APPLICATION_JSON))
                .andDo(print());

        getExpectResponseFilterData(actions, mockFilteredResources)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Entities retrieved successfully"))
                .andExpect(jsonPath("$.status").value("200 OK"))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.errors").doesNotExist())
                .andExpect(jsonPath("$.error").doesNotExist());

        Map<String, String[]> capturedParams = captor.getValue();
        for (Map.Entry<String, String[]> entry : filters.entrySet()) {
            String key = entry.getKey();
            String[] values = entry.getValue();
            assertThat(capturedParams).containsKey(key);
            assertThat(capturedParams.get(key)).containsExactly(values);
        }

        verify(service).getAll(captor.getValue());
        verify(mapper).toList(mockFilteredEntities);
    }

    /*
     * Test trường hợp lấy dữ liệu với filter -> filterComplex dạng id[gt],
     * price[lte],...
     */

    /* Test trường hợp bị lỗi Error */

}
