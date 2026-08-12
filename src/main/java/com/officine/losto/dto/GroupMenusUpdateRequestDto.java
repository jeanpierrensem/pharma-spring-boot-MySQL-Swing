package com.officine.losto.dto;

import lombok.*;

import java.util.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GroupMenusUpdateRequestDto {

    private List<Long> menuIds;
}
