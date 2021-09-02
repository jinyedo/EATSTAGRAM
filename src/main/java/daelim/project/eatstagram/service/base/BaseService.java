package daelim.project.eatstagram.service.base;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.annotation.PostConstruct;
import javax.persistence.Id;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public abstract class BaseService<K, E extends BaseEntity, D extends BaseEntity, R extends JpaRepository<E, K>> {

    @Getter @Setter(onMethod_ = @Autowired)
    private R repository;

    @Getter
    private Class<E> entityClass;

    @Getter
    private String entityId;

    @Setter @Getter
    private Map<String, DTOKey> keyMap;

    private E getEntity() {
        try {
            return getEntityClass().getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            e.printStackTrace();;
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    @PostConstruct
    public void initialize() {
        Type clazz = getClass().getGenericSuperclass();
        var type = ((ParameterizedType) clazz).getActualTypeArguments()[1];
        entityClass = (Class<E>) type;
        recursiveSuper(entityClass);
    }

    private void recursiveSuper(Class<?> clazz) {
        if(!clazz.equals(BaseEntity.class)) {
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                var idAntt = field.getAnnotation(Id.class);
                if (idAntt != null) {
                    entityId = field.getName();
                }
                DTOKey dt = field.getAnnotation(DTOKey.class);
                if (dt != null) {
                    putKeyMap(field.getName(), dt);
                }
            }
            recursiveSuper(clazz.getSuperclass());
        }
    }

    protected void  putKeyMap(String field, DTOKey key) {
        if (keyMap == null) {
            keyMap = new HashMap<>();
        }
        keyMap.put(field, key);
    }

    protected <D extends BaseEntity> void generateKey(D param) {
        try {
            if(keyMap != null) {
                for (Map.Entry<String,DTOKey> entry : keyMap.entrySet()) {
                    String fieldNm = entry.getKey();
                    DTOKey value = entry.getValue();
                    if (StringUtils.isEmpty(BeanUtils.getProperty(param, fieldNm)) && value != null) {
                        BeanUtils.setProperty(param, fieldNm, value.edtoKey().getId(value.value()));
                    }
                }
            }
        } catch (Exception e) {
            log.error("ID 생성실패", e);
        }
    }

    public <D extends BaseEntity> D save(D dto) {
        generateKey(dto);
        if (getEntityClass().equals(dto.getClass())) {
            getRepository().save(getEntityClass().cast(dto));
        }
        var e = getEntity();
        ModelMapperUtils.map(dto, e);
        getRepository().save(e);
        return dto;
    }
}
