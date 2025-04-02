package com.ddbb.dingdong.domain.transportation.repository.projection;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
public class PathPointProjection {
    private double longitude;
    private double latitude;
}