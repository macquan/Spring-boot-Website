package PMQ.local.SpringBootProject.services;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
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
import PMQ.local.SpringBootProject.specifications.BaseSpecification;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;

@Service
public abstract class BaseService<T, M extends BaseMapper<T, ?, C, U>, C, U, R extends JpaRepository<T, Long> & JpaSpecificationExecutor<T>> {
    // T: Entity type
    // R: Repository type

    @Autowired
    private ApplicationContext applicationContext;

    protected abstract String[] getSearchFields();

    protected String[] getRelations() { // Không phải là abstract method, vì không phải tất cả các service đều có quan
                                        // hệ. Nếu một service không có quan hệ, nó có thể trả về một mảng rỗng.
        return new String[0];
    }

    protected abstract R getRepository();

    protected abstract M getMapper();

    protected void preProcessRequest(C request) {

    }

    private Map<String, String[]> modifyParameters(HttpServletRequest request, Map<String, String[]> parameters) {
        Map<String, String[]> modifiedParameters = new HashMap<>(parameters);
        Object userIdAttribute = request.getAttribute("userId");
        if (userIdAttribute != null) {
            String userId = userIdAttribute.toString();
            modifiedParameters.put("userId", new String[] { userId });
        }

        return modifiedParameters;
    }

    protected JpaSpecificationExecutor<T> getSpecificationExecutor() {
        return (JpaSpecificationExecutor<T>) getRepository();
    }

    public List<T> getAll(Map<String, String[]> parameters, HttpServletRequest request) {
        Map<String, String[]> modifiedParameters = modifyParameters(request, parameters); // Gọi phương thức
                                                                                          // modifyParameters để thêm
                                                                                          // thông tin userId vào các
                                                                                          // tham số.
        Sort sort = parseSort(modifiedParameters); // Gọi phương thức parseSort để lấy thông tin sắp xếp từ các tham số.
        Specification<T> specs = buildSpecification(modifiedParameters, getSearchFields()); // Gọi phương thức
                                                                                            // buildSpecification để
                                                                                            // tạoSpecification dựa trên
                                                                                            // các tham số và trường dữ
                                                                                            // liệu "name".
        return getRepository().findAll(specs, sort); // Gọi phương thức findAll của repository để lấy danh sách tất cả
    }

    public Page<T> paginate(Map<String, String[]> parameters, HttpServletRequest request) {
        Map<String, String[]> modifiedParameters = modifyParameters(request, parameters);
        int page = modifiedParameters.containsKey("page") ? Integer.parseInt(modifiedParameters.get("page")[0]) : 1;
        int perPage = modifiedParameters.containsKey("per_page")
                ? Integer.parseInt(modifiedParameters.get("per_page")[0])
                : 20;
        Sort sort = parseSort(modifiedParameters);
        Specification<T> specs = buildSpecification(modifiedParameters, getSearchFields());

        Pageable pageable = PageRequest.of(page - 1, perPage, sort); // Tạo đối tượngPageable với số trang, số lượng bản
                                                                     // ghi trên mỗi trang và đối tượng Sort.
        return getRepository().findAll(specs, pageable);
    }

    private void handleManyToManyRelation(T entity, Object request) {
        String[] relations = getRelations();
        if (relations != null && relations.length > 0) {
            for (String relation : relations) {
                try {

                    Field requestField = request.getClass().getDeclaredField(relation);
                    requestField.setAccessible(true);

                    @SuppressWarnings("unchecked")
                    List<Long> ids = (List<Long>) requestField.get(request);
                    if (ids != null && !ids.isEmpty()) {
                        Field entityField = entity.getClass().getDeclaredField(relation);
                        entityField.setAccessible(true);

                        ParameterizedType setType = (ParameterizedType) entityField.getGenericType();
                        Class<?> entityClass = (Class<?>) setType.getActualTypeArguments()[0];

                        String repositoryName = entityClass.getSimpleName() + "Repository";
                        repositoryName = Character.toLowerCase(repositoryName.charAt(0)) + repositoryName.substring(1);

                        @SuppressWarnings("unchecked")
                        JpaRepository<T, Long> repository = (JpaRepository<T, Long>) applicationContext
                                .getBean(repositoryName);
                        List<T> entities = repository.findAllById(ids);
                        Set<T> entitySet = new HashSet<>(entities);
                        entityField.set(entity, entitySet);
                    }

                } catch (NoSuchFieldException | ClassCastException | IllegalAccessException e) {
                    throw new RuntimeException(
                            "Error handling many-to-many relation: " + relation + ": " + e.getMessage(), e);
                }
            }
        }
    }

    @Transactional
    public T create(C request) {
        preProcessRequest(request); // Gọi phương thức preProcessRequest để xử lý trước khi lưu đối tượng
                                    // UserCatalogue.
        T payload = getMapper().toEntity(request); // Gọi phương thức toEntity của mapper để chuyển đổi
                                                   // StoreRequest thành đối tượng UserCatalogue.
        T entity = getRepository().save(payload); // Gọi phương thức save của repository để lưu đối tượng UserCatalogue
        handleManyToManyRelation(entity, request); // Gọi phương thức handleManyToManyRelationships để xử lý
                                                   // các mối quan hệ nhiều-nhiều (nếu có).

        return entity; // Trả về đối tượng UserCatalogue đã được lưu
    }

    @Transactional
    public T update(Long id, U request) {
        T entity = getRepository().findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Entity not found with id: " + id));
        getMapper().updateEntityFromRequest(request, entity);
        T entityUpdate = getRepository().save(entity); // Gọi phương thức save của repository để lưu đối tượng
                                                       // UserCatalogue
        handleManyToManyRelation(entityUpdate, request); // Gọi phương thức handleManyToManyRelationships để xử lý các
                                                         // mối quan hệ nhiều-nhiều (nếu có).

        return getRepository().save(entityUpdate);
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
