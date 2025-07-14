package com.stanzaliving.secure.dto;

import lombok.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author naveen
 * @date 29-Sep-2019
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PageResponse<T> implements Serializable {

	private static final long serialVersionUID = 1L;

	private int page;

	private int records;

	private int totalPages;

	private long totalRecords;

	private List<T> data;

    private Map<String, Object> extraInfo;

    public PageResponse(int page, int records, int totalPages, long totalRecords, List<T> data) {
        this.page = page;
        this.records = records;
        this.totalPages = totalPages;
        this.totalRecords = totalRecords;
        this.data = data;
    }

    public static <T> PageResponse<T> of(int page, int records, int totalPages, long totalRecords) {
        return new PageResponse<>(page, records, totalPages, totalRecords, new ArrayList<>());
    }

	public static <T> PageResponse<T> of(int page, int records, int totalPages, long totalRecords, List<T> data) {
		return new PageResponse<>(page, records, totalPages, totalRecords, data);
	}

	public static <T> PageResponse<T> of(int page, int limit, List<T> data) {
		int start = (page - 1) * limit;
		int end = ((page - 1) * limit + limit);
		int sizeOfList = data.size();
		if (start > sizeOfList - 1) {
			return PageResponse.of(page, 0, (int) Math.ceil((double) data.size() / (double) limit), data.size(), new ArrayList<>());
		}
		if (end > sizeOfList) {
			end = sizeOfList;
		}
		return PageResponse.of(page, data.subList(start, end).size(), (int) Math.ceil((double) data.size() / (double) limit), data.size(), data.subList(start, end));
	}
}