package zinsoft.faas.service;

import java.util.List;

import zinsoft.faas.dto.CalDto;

public interface CalService {

    String get(String calDt, String calTCd);

    List<CalDto> list(String startDt, String endDt);

}