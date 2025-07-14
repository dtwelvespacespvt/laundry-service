/**
 * 
 */
package com.stanzaliving.secure.dto;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

/**
 * 
 * @author naveen
 *
 * @date 29-Sep-2019
 */
@Getter
@Setter
@ToString
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class PaginationRequest implements Serializable {

	@Builder.Default
	protected int pageNo = 1;

	@Builder.Default
	protected int limit = 100;

}