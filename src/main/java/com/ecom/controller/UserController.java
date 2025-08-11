package com.ecom.controller;
import com.ecom.model.Cart;
import com.ecom.model.Category;
import com.ecom.model.OrderRequest;
import com.ecom.model.UserDtls;
import com.ecom.services.CartService;
import com.ecom.services.CategoryService;
import com.ecom.services.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CartService cartService;


    @GetMapping("/")
    public String home() {
        return "user/home";
    }

    @Autowired
    private UserService userService;

    @ModelAttribute
    public void getUserDetails(Principal p, Model m){
        if(p!=null){
            String email=p.getName();
            UserDtls userDtls=userService.getUserByEmail(email);
            m.addAttribute("user", userDtls);
            Integer countCart=cartService.getCountCart(userDtls.getId());
            m.addAttribute("countCart",countCart);
        }
        List<Category> allActiveCategory = categoryService.getAllActiveCategory();
        m.addAttribute("categorys",allActiveCategory);
    }


@GetMapping("/addCart")
    public String addCart(@RequestParam Integer pid, @RequestParam Integer uid, HttpSession session){
    Cart saveCart = cartService.saveCart(pid, uid);
    if(ObjectUtils.isEmpty(saveCart)){
        session.setAttribute("errorMsg", "Product add to cart failed");
    }else{
        session.setAttribute("succMsg","Product added to cart");
    }
    return "redirect:/product/"+pid;
    }

    @GetMapping("/cart")
 public String loadCartPage(Principal p,Model m)
    {

        UserDtls user=getLoggedInUserDetails(p);
        List<Cart>carts=cartService.getCartByUser(user.getId());
        m.addAttribute("carts",carts);
        if (carts.size()>0) {
            Double totalOrderPrice = carts.get(carts.size() - 1).getTotalOrderPrice();
            m.addAttribute("totalOrderPrice", totalOrderPrice);
        }
        return "/user/cart";
 }

 @GetMapping("/cartQuantityUpdate")
 public String updateCartQuantity(@RequestParam String sy, @RequestParam Integer cid){
        cartService.updateQuantity(sy,cid);
        return "redirect:/user/cart";
 }

    private UserDtls getLoggedInUserDetails(Principal p) {
        String email= p.getName();
        UserDtls userDtls=userService.getUserByEmail(email);
        return userDtls;
    }

@GetMapping("/orders")
    private String orderPage(Principal p,Model m){
    UserDtls user=getLoggedInUserDetails(p);
    List<Cart>carts=cartService.getCartByUser(user.getId());
    m.addAttribute("carts",carts);
    if (carts.size()>0) {
        Double orderPrice = carts.get(carts.size() - 1).getTotalOrderPrice();
        Double totalOrderPrice = carts.get(carts.size() - 1).getTotalOrderPrice()+250+100;
        m.addAttribute("orderPrice", orderPrice);
        m.addAttribute("totalOrderPrice", totalOrderPrice);
    }
        return "user/order";
    }

    @PostMapping("/save-order")
    private String orderOrder(@ModelAttribute OrderRequest request) {
        System.out.println();
        return "redirect:/user/success";
    }

    @GetMapping("/success")
    public String loadSuccess(){
        return "/user/success";
    }
}
