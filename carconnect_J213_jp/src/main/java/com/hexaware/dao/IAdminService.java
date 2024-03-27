package com.hexaware.dao;

import com.hexaware.entity.Admin;

public interface IAdminService {
    Admin getAdminById(int adminId);

    Admin getAdminByUsername(String username);

    void registerAdmin(Admin adminData);

    void updateAdmin(Admin adminData);

    void deleteAdmin(int adminId);
}
