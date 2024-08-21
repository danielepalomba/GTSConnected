package org.app.gtsconnected3.service;

import org.app.gtsconnected3.entity.User;

public interface PasswordResetServiceInt {
    void updatePassword(User user, String newPassword);
    User get(String resetPasswordToken);
}
