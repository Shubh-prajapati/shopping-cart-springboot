package com.ecom.controller;
import com.ecom.model.Category;
import com.ecom.model.Product;
import com.ecom.model.ProductOrder;
import com.ecom.model.UserDtls;
import com.ecom.services.*;
import com.ecom.util.CommonUtil;
import com.ecom.util.OrderStatus;
import jakarta.servlet.http.HttpSession;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;


@Controller
@RequestMapping("/admin")
    public class AdminController {

        @Autowired
        private UserService userService;
        @Autowired
        private CategoryService categoryService;
        @Autowired
        private  ProductService productService;

        @Autowired
        private CartService cartService;

        @Autowired
        private OrderService orderService;

        @Autowired
         private CommonUtil commonUtils;


        @Autowired
        private PasswordEncoder passwordEncoder;



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

    @GetMapping("/")
        public String index()
        {
            return "admin/index";
        }

        @GetMapping("/loadAddProduct")
        public String loadAddProduct(Model m)
        {
            List<Category> categories = categoryService.getAllCategory();
            m.addAttribute("categories",categories);
            return "admin/add_product";
        }
        @GetMapping("/category")
        public String category(Model m,@RequestParam(name = "pageNo",defaultValue = "0")Integer pageNo,@RequestParam(name = "pageSize",defaultValue = "2")Integer pageSize) {
//            m.addAttribute("categorys",categoryService.getAllCategory());

            Page<Category> page=categoryService.getAllCategoryPagination(pageNo,pageSize);
            List<Category>categorys=page.getContent();
            m.addAttribute("categorys",categorys);


            m.addAttribute("pageNo",page.getNumber());
            m.addAttribute("pageSize",pageSize);
            m.addAttribute("totalElements",page.getTotalElements());
            m.addAttribute("totalPages",page.getTotalPages());
            m.addAttribute("isFirst",page.isFirst());
            m.addAttribute("isLast",page.isLast());
            return "admin/category";
        }
        @SneakyThrows
        @PostMapping("/saveCategory")
        public String saveCategory(@ModelAttribute Category category, @RequestParam("file") MultipartFile file, HttpSession session){
            String imageName =file !=null ? file.getOriginalFilename(): "default.jpg";
            category.setImageName(imageName);

           Boolean existCategory = categoryService.existCategory(category.getName());
            if (existCategory)
            {
                session.setAttribute("errorMsg", "Category Name already exits");
            }else {
                Category saveCategory = categoryService.saveCategory(category);
                if(ObjectUtils.isEmpty(saveCategory)){
                    session.setAttribute("errorMsg","Not saved ! internal server error");
                }else {
                    String uploadDir = "/app/uploads/category_img";
                    Path uploadPath = Paths.get(uploadDir);

                    if (!Files.exists(uploadPath)) {
                        Files.createDirectories(uploadPath);
                    }

                    Path path = uploadPath.resolve(file.getOriginalFilename());
                    Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

                    session.setAttribute("succMsg", "Saved successfully");
                }
            }
                categoryService.saveCategory(category);

            return "redirect:/admin/category";
        }
        @GetMapping("/deleteCategory/{id}")
        public String deleteCategory(@PathVariable int id, HttpSession session)
        {
          Boolean deleteCategory= categoryService.deleteCategory(id);
          if(deleteCategory)
          {
              session.setAttribute("succMsg","category delete success");
          }else {
              session.setAttribute("errorMsg","something wrong on server");
          }
            return "redirect:/admin/category";
        }
        @GetMapping("/loadEditCategory/{id}")
        public String loadEditCategory(@PathVariable int id, Model m){
            m.addAttribute("category",categoryService.getCategoryById(id));
            return "admin/editCategory";
        }
            @PostMapping("/updateCategory")
            public String updateCategory(@ModelAttribute Category category, @RequestParam("file") MultipartFile file,HttpSession session) throws IOException {
            Category oldcategory=categoryService.getCategoryById(category.getId());
            String imageName = file.isEmpty() ?  oldcategory.getImageName(): file.getOriginalFilename();
            if(!ObjectUtils.isEmpty(category)) {
                oldcategory.setName(category.getName());
//             oldcategory.setIsActive(category.getIsActive());
                oldcategory.setImageName(imageName);
            }
            Category updateCategory =categoryService.saveCategory(oldcategory);

            if(!ObjectUtils.isEmpty(updateCategory))
            {
                    if(!file.isEmpty()){
                        File saveFile=new ClassPathResource("static/img").getFile();
                        Path path= Paths.get(saveFile.getAbsolutePath()+ File.separator +"category_img"+ File.separator+ file.getOriginalFilename());
                        //System.out.println(path);
                        Files.copy(file.getInputStream(),path, StandardCopyOption.REPLACE_EXISTING);
                    }

                session.setAttribute("succMsg","Category update success");
            }
            else{
                session.setAttribute("errorMsg","something wrong on server");

            }
            return "redirect:/admin/loadEditCategory/" +category.getId();
        }

    @PostMapping("/saveProduct")
    public String saveProduct(@ModelAttribute Product product,
                              @RequestParam("file") MultipartFile image,
                              HttpSession session) throws IOException {

        String imageName = image.isEmpty() ? "default.jpg" : image.getOriginalFilename();
        product.setImage(imageName);
        product.setDiscount(0);
        product.setDiscountPrice(product.getPrice());

        Product saveProduct = productService.saveproduct(product);

        if (!ObjectUtils.isEmpty(saveProduct)) {

            String uploadDir = "/app/uploads/product_img";
            Path uploadPath = Paths.get(uploadDir);

            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            Path path = uploadPath.resolve(image.getOriginalFilename());
            Files.copy(image.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

            session.setAttribute("succMsg", "Product Saved Success");
        } else {
            session.setAttribute("errorMsg", "something wrong on server");
        }

        return "redirect:/admin/loadAddProduct";
    }


           @GetMapping("/products")
           public String loadViewProduct(Model m, @RequestParam(name = "ch", defaultValue = "") String ch,
                                         @RequestParam(name="pageNo",defaultValue = "0")Integer pageNo,
                                         @RequestParam(name="pageSize",defaultValue = "10") Integer pageSize){
//               List<Product> products=null;
//            if (ch!=null && ch.length()>0){
//                products= productService.searchProduct(ch);
//            }else {
//               products= productService.getAllProduct();
//            }
//            m.addAttribute("products",products);

               Page<Product> page=null;
               if (ch!=null && ch.length()>0){
                  page= productService.searchProductPagination(pageNo,pageSize,ch);
               }else {
                   page= productService.getAllProductPagination(pageNo, pageSize);
               }
               m.addAttribute("products",page.getContent());
               m.addAttribute("pageNo",page.getNumber());
               m.addAttribute("pageSize",pageSize);
               m.addAttribute("totalElements",page.getTotalElements());
               m.addAttribute("totalPages",page.getTotalPages());
               m.addAttribute("isFirst",page.isFirst());
               m.addAttribute("isLast",page.isLast());
            return "admin/products";

           }

    @GetMapping("/deleteProduct/{id}")
    public String deleteProduct(@PathVariable int id, HttpSession session){
           Boolean deleteProduct= productService.deleteProduct(id);

           if (deleteProduct){
                session.setAttribute("succMsg","Product delete success");
           }else{
               session.setAttribute("errorMsg","Something wrong on server");
           }
        return "redirect:/admin/products";

    }

    @GetMapping("/editProduct/{id}")
    public String editProduct(@PathVariable int id ,Model m){
         m.addAttribute("product", productService.getProductById(id));
         m.addAttribute("categories",categoryService.getAllCategory());
        return "admin/edit_product";

    }

    @PostMapping("/updateProduct")
    public String updateProduct(@ModelAttribute Product product,@RequestParam("file") MultipartFile image,HttpSession session,Model m){


            if(product.getDiscount() < 0 || product.getDiscount() >100){

                session.setAttribute("errorMsg","invalid Discount");
            }else {
                Product updateProduct = productService.updateProduct(product,image);
            if (!ObjectUtils.isEmpty(updateProduct))
            {
                session.setAttribute("succMsg","Product update success");

            }else {
                session.setAttribute("errorMsg","Something wrong on server");
             }
            }
        return "redirect:/admin/editProduct/"+product.getId();

    }

    @GetMapping("/users")
    public String getAllUser(Model m,@RequestParam Integer type){

        List<UserDtls> users=null;
            if(type==1){
               users= userService.getUsers("ROLE_USER");
            }else{
                users= userService.getUsers("ROLE_ADMIN");
            }
            m.addAttribute("userType",type);
            m.addAttribute("users",users);
        return "/admin/users";
    }

    @GetMapping("/updateSts")
    public String updateUserAccountStatus(@RequestParam Boolean status,@RequestParam Integer id,@RequestParam Integer type, HttpSession session){
           Boolean f= userService.updateAccountStatus(id,status);
           if(f){
               session.setAttribute("succMsg", "Account Status Updated");
           }else {
               session.setAttribute("errorMsg","Something Wrong On Server");
           }

            return "redirect:/admin/users?type="+type;
    }

    @GetMapping("/orders")
    public String getAllOrders(Model m, @RequestParam(name="pageNo",defaultValue = "0")Integer pageNo,
                               @RequestParam(name="pageSize",defaultValue = "2") Integer pageSize){
//        List<ProductOrder>allOrder=orderService.getAllOrder();
//        m.addAttribute("orders",allOrder);
//        m.addAttribute("srch", false);


        Page<ProductOrder>page=orderService.getAllOrdersPagination(pageNo,pageSize);
        m.addAttribute("orders",page.getContent());
        m.addAttribute("srch", false);

        m.addAttribute("pageNo",page.getNumber());
        m.addAttribute("pageSize",pageSize);
        m.addAttribute("totalElements",page.getTotalElements());
        m.addAttribute("totalPages",page.getTotalPages());
        m.addAttribute("isFirst",page.isFirst());
        m.addAttribute("isLast",page.isLast());



        return "/admin/orders";
    }

    @PostMapping("/update-order-status")
    public String updateOrderStatus(@RequestParam Integer id,
                                    @RequestParam Integer st,
                                    HttpSession session) {

        OrderStatus[] values = OrderStatus.values();
        String status = null;

        for (OrderStatus orderSt : values) {
            if (orderSt.getId().equals(st)) {
                status = orderSt.name();   // IMPORTANT CHANGE
            }
        }

        ProductOrder updateOrder = orderService.updateOrderStatus(id, status);

        if (!ObjectUtils.isEmpty(updateOrder)) {
            session.setAttribute("succMsg", "Status Updated");

            try {
                commonUtils.sendMailForProductOrder(updateOrder, status);
            } catch (Exception e) {
                e.printStackTrace();
                session.setAttribute("errorMsg", "Status updated but email not sent");
            }

        } else {
            session.setAttribute("errorMsg", "Status Not Updated");
        }

        return "redirect:/admin/orders";
    }
    @GetMapping("/search-order")
    public String searchProduct(@RequestParam String orderId, Model m, HttpSession session,@RequestParam(name="pageNo",defaultValue = "0")Integer pageNo,
                                @RequestParam(name="pageSize",defaultValue = "2") Integer pageSize){
            ProductOrder order=orderService.getOrderByOrderId(orderId.trim());
            if(orderId!=null && orderId.length()>0) {

                if (ObjectUtils.isEmpty(order)) {
                    session.setAttribute("errorMsg", "Incorrect orderId");
                    m.addAttribute("orderDtls", null);
                } else {
                    m.addAttribute("orderDtls", order);
                }
                m.addAttribute("srch", true);
            }else {
//                List<ProductOrder>allOrder=orderService.getAllOrder();
//                m.addAttribute("orders",allOrder);
//                m.addAttribute("srch", false);

                Page<ProductOrder>page=orderService.getAllOrdersPagination(pageNo,pageSize);
                m.addAttribute("orders",page);
                m.addAttribute("srch", false);


                m.addAttribute("pageNo",page.getNumber());
                m.addAttribute("pageSize",pageSize);
                m.addAttribute("totalElements",page.getTotalElements());
                m.addAttribute("totalPages",page.getTotalPages());
                m.addAttribute("isFirst",page.isFirst());
                m.addAttribute("isLast",page.isLast());

            }
        return "/admin/orders";
    }


@GetMapping("/add-admin")
    public String loadAdminAdd(){
            return "/admin/add_admin";
    }


    @PostMapping("/admin/save-admin")
    public String saveAdmin(@ModelAttribute UserDtls user,
                            @RequestParam("img") MultipartFile file,
                            HttpSession session) throws IOException {
        String imageName = file.isEmpty() ? "default.jpg" : file.getOriginalFilename();
        user.setProfileImage(imageName);
        UserDtls saveUser = userService.saveAdmin(user);

        if (!ObjectUtils.isEmpty(saveUser)) {
            if (!file.isEmpty()) {
                File saveFile = new ClassPathResource("static/img").getFile();
                Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + "profile_img"
                        + File.separator + file.getOriginalFilename());
                Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
            }
            session.setAttribute("succMsg", "Register Successfully");
        } else {
            session.setAttribute("errorMsg", "Something went wrong on the server");
        }

        return "redirect:/admin/add-admin";
    }

    @GetMapping("/profile")
    public String profile(){
            return "admin/profile";
    }
    @PostMapping("/update-profile")
    public String updateProfile(@ModelAttribute UserDtls user, @RequestParam MultipartFile img, HttpSession session){

        UserDtls updateUserProfile = userService.updateUserProfile(user, img);
        if(ObjectUtils.isEmpty(updateUserProfile)){
            session.setAttribute("errorMsg", "Profile Not Updated");
        }else {
            session.setAttribute("succMsg","Profile Updated");
        }
        return "redirect:/admin/profile";
    }


    @PostMapping("/change-password")
    public String changePassword(@RequestParam String  newPassword, String currentPassword,Principal p,HttpSession session){

        UserDtls loggedInUserDetails = commonUtils.getLoggedInUserDetails(p);

        boolean matches=passwordEncoder.matches(currentPassword,loggedInUserDetails.getPassword());
        if(matches){
            String encodePassword=passwordEncoder .encode(newPassword);
            loggedInUserDetails.setPassword(encodePassword);
            UserDtls updateUser=userService.updateUser(loggedInUserDetails);
            if (ObjectUtils.isEmpty(updateUser)){
                session.setAttribute("error","password not updated !! Error in server");
            }else {
                session.setAttribute("succMsg","Password Update sucessfully");
            }
        }else {
            session.setAttribute("errorMsg","Current Password incorrect");
        }
        return "redirect:/admin/profile";
    }
}

