package zinsoft.web.common.controller;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import zinsoft.faas.dto.CropDto;
import zinsoft.faas.dto.CropSpeciesDto;
import zinsoft.faas.entity.EpisFsHervInfo;
import zinsoft.faas.service.CropService;
import zinsoft.faas.service.impl.CropSpeciesService;
import zinsoft.util.DataTablesResponse;
import zinsoft.util.Result;

@Controller
@RequestMapping("${api.prefix}/crop")
public class CropController {

    @Resource
    CropService cropService;

    @Autowired
    private CropSpeciesService cropSpeciesService;

    @GetMapping(value = "/{cropSeq}")
    @ResponseBody
    public CropDto get(@PathVariable("cropSeq") Long cropSeq) throws Exception {

        return cropService.get( cropSeq);
    }

    @PostMapping(value = "")
    @ResponseBody
    public Result insert(@Valid CropDto dto) throws Exception {

        cropService.insert(dto);

        return new Result(true, Result.OK);
    }

    @GetMapping(value = "/page")
    @ResponseBody
    public DataTablesResponse<CropDto> page(@RequestParam Map<String, Object> search, @PageableDefault Pageable pageable, HttpSession session) throws Exception {

        return cropService.page(search, pageable);
    }

    @GetMapping(value = "/list")
    @ResponseBody
    public Result list(@RequestParam Map<String, Object> map) throws Exception {
        //Map<String, Object> map = new HashMap<>();
        List<CropDto> list = cropService.list(map);
        return new Result(true, "0000", list);
    }

    @GetMapping(value = "/species/{cropBCd}")
    @ResponseBody
    public Result getCropSpeciesList(@PathVariable String cropBCd) throws Exception {
        List<CropSpeciesDto> speciesDTOList = cropSpeciesService.getAllByCropBCd(cropBCd);
        return new Result(true, "0000", speciesDTOList);
    }

    @GetMapping(value = "/shape/list")
    @ResponseBody
    public Result getTest() throws Exception {
        List<EpisFsHervInfo> shapes = cropService.getCropShapeList();
        return new Result(true, "0000", shapes);
    }

    @PutMapping(value = "/{cropSeq}")
    @ResponseBody
    public Result put(@PathVariable("cropSeq") Long cropSeq, @Valid CropDto vo) throws Exception {
        cropService.update(vo);

        return new Result(true, "0000");
    }

    @DeleteMapping(value = "/{cropSeq}")
    @ResponseBody
    public Result delete(@PathVariable("cropSeq") Long cropSeq) throws Exception {
        cropService.delete(new Long[]{cropSeq});
        return new Result(true, "0000");
    }


}
