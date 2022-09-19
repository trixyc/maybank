package com.maybank.util;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
public class MapperUtil {
	public static final ModelMapper modelMapper = new ModelMapper();

	/**
	 * Maps the Page {@code entities} of <code>T</code> type which have to be mapped as input to {@code dtoClass} Page
	 * of mapped object with <code>D</code> type.
	 *
	 * @param <D> - type of objects in result page
	 * @param <T> - type of entity in <code>entityPage</code>
	 * @param entities - page of entities that needs to be mapped
	 * @param dtoClass - class of result page element
	 * @return page - mapped page with objects of type <code>D</code>.
	 * @NB <code>dtoClass</code> must has NoArgsConstructor!
	 */
	public <D, T> Page<D> entityPageToDtoPage(Page<T> entities, Class<D> dtoClass) {
	    return entities.map(objectEntity -> modelMapper.map(objectEntity, dtoClass));
	}
}
