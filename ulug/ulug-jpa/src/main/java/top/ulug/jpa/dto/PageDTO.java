package top.ulug.jpa.dto;

import org.springframework.data.domain.Page;

import java.io.Serializable;
import java.util.List;

/**
 * Created by fddiljf on 2017/5/4.
 * 逝者如斯夫 不舍昼夜
 */
public class PageDTO<T> implements Serializable {
    private int number;
    private int numberOfElements;
    private int size;
    private long totalElements;
    private long totalPages;
    private List<T> content;

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getNumberOfElements() {
        return numberOfElements;
    }

    public void setNumberOfElements(int numberOfElements) {
        this.numberOfElements = numberOfElements;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(long totalElements) {
        this.totalElements = totalElements;
    }

    public long getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(long totalPages) {
        this.totalPages = totalPages;
    }

    public List<T> getContent() {
        return content;
    }

    public void setContent(List<T> content) {
        this.content = content;
    }

    public PageDTO<T> convert(Page<T> page) {
        this.setContent(page.getContent());
        this.setNumber(page.getNumber());
        this.setSize(page.getSize());
        this.setTotalElements(page.getTotalElements());
        this.setTotalPages(page.getTotalPages());
        this.setNumberOfElements(page.getNumberOfElements());
        return this;
    }
}
