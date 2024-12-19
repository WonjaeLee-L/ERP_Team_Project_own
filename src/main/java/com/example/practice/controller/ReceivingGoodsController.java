package com.example.practice.controller;

import com.example.practice.vo.Pagevo;
import com.example.practice.vo.ReceivingPageVO;
import com.example.practice.vo.SlipVO;
import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ReceivingGoodsController {

    @GetMapping("/receivingGoods")
    public ModelAndView showInCargoList(Model model, @ModelAttribute ReceivingPageVO rpagevo) throws Exception {
        if (rpagevo.getPage() == null) {
            rpagevo.setPage(1);
        }

//        rpagevo.setTotalCount(receivinggoodsservice.totalCountPV());

//        List<GoodsVO> slipList = receivinggoodsservice.selectAll(rpagevo);  // 전표 목록 가져오기
        ModelAndView mv = new ModelAndView("inCargo");
//        model.addAttribute("goods", inCargo);  // 전표 목록을 "slips"라는 이름으로 모델에 추가

        return mv;
    }


}
