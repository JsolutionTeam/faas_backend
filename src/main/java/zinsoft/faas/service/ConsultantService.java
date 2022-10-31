package zinsoft.faas.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zinsoft.faas.dto.ConsultantResDto;
import zinsoft.faas.repository.ConsultantQueryRepository;

import java.util.List;

@Service
public class ConsultantService{

    @Autowired
    private ConsultantQueryRepository consultantQueryRepository;

    public String findMyConsult(){
        List<ConsultantResDto> consults = consultantQueryRepository.findMyConsultant();
        // 한 농장에 하나씩밖에 안 키운다곤 하지만
        // 추후에 어떠한 일이 일어날지 몰라서 일단 List로 만들어둠. 나중에 여러개를 분석해서 찾아야 한다면
        // filter같은걸 사용하면 될 듯.
        if(consults.size() > 0){
            return consults.get(0).getWorkOrder();
        }
        return "컨설턴트 내용이 없습니다.";
    }
}
