package ru.yreyaaa.taskmanagementsystem.TaskManagementSystem.util;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.data.jpa.domain.Specification;
import ru.yreyaaa.taskmanagementsystem.TaskManagementSystem.models.Task;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TaskSpecification implements Specification<Task> {

    private final JSONObject criteria;
    private List<Predicate> filters;

    public TaskSpecification(JSONObject criteria) {
        this.criteria = criteria;
    }

    @Override
    public Predicate toPredicate(Root<Task> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
        Iterator<?> keys = criteria.keys();
        List<Predicate> filters = new ArrayList<>();

        if (criteria.length() != 0) {

            while (keys.hasNext()) {
                String key = (String) keys.next();
                String filterValue = null;

                try {
                    filterValue = criteria.get(key).toString();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (filterValue != null) {
                    filters.add(criteriaBuilder.like(criteriaBuilder.upper(root.get(key)), "%" + filterValue.toUpperCase() + "%"));
                }
            }
        }
        //this is the point : didn't know we could concatenate multiple predicates into one.
        return criteriaBuilder.and(filters.toArray(new Predicate[filters.size()]));
    }
}