package com.geekbrains.bootdata.controllers;

import com.geekbrains.bootdata.entities.Item;
import com.geekbrains.bootdata.repositories.ItemRepository;
import com.geekbrains.bootdata.repositories.ItemSpecifications;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class MainController {
    @Autowired
    private ItemRepository itemRepository;

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/items")
    @ResponseBody
    public List<Item> getAllItems() {
        return itemRepository.findAll();
    }

    @GetMapping("/filteringAndPaging")
    public String pagingExample(Model model,
                                @RequestParam(required = false, name = "min_price") Integer minPrice,
                                @RequestParam(required = false, name = "max_price") Integer maxPrice,
                                @RequestParam(required = false, name = "word") String word) {

        Specification<Item> spec = Specification.where(null);
        if (minPrice != null) {
            spec = spec.and(ItemSpecifications.priceGreaterThanOrEq(minPrice));
        }
        if (maxPrice != null) {
            spec = spec.and(ItemSpecifications.priceLesserThanOrEq(maxPrice));
        }
        if (word != null) {
            spec = spec.and(ItemSpecifications.titleContains(word));
        }
        Page<Item> page = itemRepository.findAll(spec, PageRequest.of(0, 5, Sort.Direction.ASC, "price"));
        model.addAttribute("items", page.getContent());
        model.addAttribute("itemsCount", page.getTotalElements());
        return "list";
    }


}
