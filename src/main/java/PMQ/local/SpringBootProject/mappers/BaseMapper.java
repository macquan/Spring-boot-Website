package PMQ.local.SpringBootProject.mappers;

import java.util.List;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.data.domain.Page;

// Entity: E, Resource: R, CreateRequest: C, UpdateRequest: U
public interface BaseMapper<E, R, C, U> {
    // Chuyển đổi từ Entity sang Resource
    R toResource(E entity);

    // Chuyển đổi từ List<Entity> sang List<Resource>
    List<R> toList(List<E> entities);

    default Page<R> toResourcePage(Page<E> page) {
        return page.map(this::toResource);
    }

    // BeanMapping được sử dụng để cấu hình cách ánh xạ giữa các đối tượng. Ở đây,
    // chúng ta chỉ định rằng khi ánh xạ từ UpdateRequest sang Entity, nếu một thuộc
    // tính trong UpdateRequest có giá trị null, thì không nên ghi đè giá trị tương
    // ứng trong Entity.
    @Mapping(target = "id", ignore = true) // Bỏ qua trường id khi ánh xạ từ CreateRequest sang Entity
    @Mapping(target = "createdAt", ignore = true) // Bỏ qua trường createdAt khi ánh xạ từ CreateRequest sang Entity
    @Mapping(target = "updatedAt", ignore = true) // Bỏ qua trường updated
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    E toEntity(C createRequest);

    // BeanMapping được sử dụng để cấu hình cách ánh xạ giữa các đối tượng. Ở đây,
    // chúng ta chỉ định rằng khi ánh xạ từ UpdateRequest sang Entity, nếu một thuộc
    // tính trong UpdateRequest có giá trị null, thì không nên ghi đè giá trị tương
    // ứng trong Entity. Phương thức này cũng sử dụng @MappingTarget để chỉ định
    // rằng chúng ta đang cập nhật một thực thể hiện có thay vì tạo một thực thể
    // mới.
    @Mapping(target = "id", ignore = true) // Bỏ qua trường id khi ánh xạ từ UpdateRequest sang Entity
    @Mapping(target = "createdAt", ignore = true) // Bỏ qua trường createdAt khi ánh xạ từ UpdateRequest sang Entity
    @Mapping(target = "updatedAt", ignore = true) // Bỏ qua trường updatedAt khi ánh xạ từ UpdateRequest sang Entity
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromRequest(U updateRequest, @MappingTarget E entity);
}
