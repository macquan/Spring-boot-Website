package PMQ.local.SpringBootProject.services;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Service;

import PMQ.local.SpringBootProject.helpers.FilterParameters;
import PMQ.local.SpringBootProject.mappers.BaseMapper;
import PMQ.local.SpringBootProject.modules.users.entities.UserCatalogue;
import PMQ.local.SpringBootProject.specifications.BaseSpecification;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public abstract class BaseService<T, M extends BaseMapper<T, ?, C, U>, C, U, R extends JpaRepository<T, Long> & JpaSpecificationExecutor<T>> {
    // T: Entity type
    // R: Repository type
    protected abstract String[] getSearchFields();

    protected abstract R getRepository();

    protected abstract M getMapper();

    protected JpaSpecificationExecutor<T> getSpecificationExecutor() {
        return (JpaSpecificationExecutor<T>) getRepository();
    }

    public List<T> getAll(Map<String, String[]> parameters) {
        Sort sort = parseSort(parameters); // Gọi phương thức parseSort để lấy thông tin sắp xếp từ các tham số.
        Specification<T> specs = buildSpecification(parameters, getSearchFields()); // Gọi phương thức
                                                                                    // buildSpecification để tạo
                                                                                    // Specification dựa trên các tham
                                                                                    // số và trường dữ liệu "name".
        return getRepository().findAll(specs, sort); // Gọi phương thức findAll của repository để lấy danh sách tất cả
    }

    public Page<T> paginate(Map<String, String[]> parameters) {
        int page = parameters.containsKey("page") ? Integer.parseInt(parameters.get("page")[0]) : 1;
        int perPage = parameters.containsKey("per_page") ? Integer.parseInt(parameters.get("per_page")[0]) : 20;
        Sort sort = parseSort(parameters);
        Specification<T> specs = buildSpecification(parameters, getSearchFields());

        Pageable pageable = PageRequest.of(page - 1, perPage, sort); // Tạo đối tượngPageable với số trang, số lượng bản
                                                                     // ghi trên mỗi trang và đối tượng Sort.
        return getRepository().findAll(specs, pageable);
    }

    @Transactional
    public T create(C request) {
        T payload = getMapper().toEntity(request); // Gọi phương thức toEntity của mapper để chuyển đổi
                                                   // StoreRequest thành đối tượng UserCatalogue.
        return getRepository().save(payload); // Gọi phương thức save của repository để lưu đối tượng UserCatalogue
                                              // vào cơ sở dữ liệu.
    }

    @Transactional
    public T update(Long id, U request) {

        T entity = getRepository().findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Entity not found with id: " + id));

        getMapper().updateEntityFromRequest(request, entity);

        return getRepository().save(entity);
    }

    @Transactional
    public Boolean delete(Long id) {
        getRepository().findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Entity not found with id: " + id));

        getRepository().deleteById(id);

        return true;
    }

    @Transactional
    public Boolean deleteMultipleEntity(List<Long> ids) {
        List<T> entities = getRepository().findAllById(ids);
        if (entities.size() != ids.size()) {
            throw new EntityNotFoundException(
                    "The number of found entities does not match the number of requested IDs.");
        }
        getRepository().deleteAll(entities);
        return true;
    }

    public Specification<T> buildSpecification(Map<String, String[]> parameters, String[] searchFields) {
        String keyword = FilterParameters.filterKeyword(parameters);
        Map<String, String> filterSimple = FilterParameters.filterSimple(parameters);
        Map<String, Map<String, String>> filterComplex = FilterParameters.filterComplex(parameters);

        Specification<T> specs = Specification
                .where(BaseSpecification.<T>keyword(keyword, searchFields)) // Tạo Specification dựa trên từ
                                                                            // khóa và các trường dữ liệu.
                .and(BaseSpecification.<T>whereSpec(filterSimple))
                .and(BaseSpecification.<T>complexWhereSpec(filterComplex)); // Tạo Specification dựa trên
                                                                            // các bộ lọc
                                                                            // phức tạp.
        return specs;
    }

    protected Sort createSort(String sortParam) {
        if (sortParam == null || sortParam.isEmpty()) {
            return Sort.by(Sort.Order.desc("id"));
        }

        String[] parts = sortParam.split(","); // Dùng để tách chuỗi sortParam thành các phần tử dựa trên dấu phẩy.
        String field = parts[0]; // Lấy tên trường để sắp xếp từ phần tử đầu tiên.
        String sortDirection = (parts.length > 1) ? parts[1] : "asc"; // Lấy hướng sắp xếp từ phần tử thứ hai, mặc định
                                                                      // là "asc" nếu không có.

        if ("desc".equalsIgnoreCase(sortDirection)) { // Kiểm tra xem hướng sắp xếp có phải là "desc" hay không (không
                                                      // phân biệt chữ hoa chữ thường).
            return Sort.by(Sort.Order.desc(field));
        } else {
            return Sort.by(Sort.Order.asc(field));
        }
    }

    // Phương thức parseSort được sử dụng để phân tích các tham số sắp xếp từ một
    // bản đồ các tham số (parameters) và trả về một đối tượng Sort tương ứng. Nó
    // kiểm tra xem có tồn tại tham số "sort" trong bản đồ hay không, nếu có thì lấy
    // giá trị của nó, nếu không thì trả về null. Sau đó, nó gọi phương thức
    // createSort để tạo đối tượng Sort dựa trên giá trị của sortParam.
    protected Sort parseSort(Map<String, String[]> parameters) {
        String sortParam = parameters.containsKey("sort") ? parameters.get("sort")[0] : null;
        return createSort(sortParam);
    }
}
