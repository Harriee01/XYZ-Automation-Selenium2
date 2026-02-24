package com.xyz.tests.manager;

import com.xyz.base.BaseTest;
import com.xyz.models.TestData;
import com.xyz.pages.*;
import com.xyz.utils.TableAssertions;
import io.qameta.allure.*;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


//ManagerTests — all 8 Bank Manager user story test cases


@Epic("XYZ Bank")
@Feature("Bank Manager Operations")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)

public class ManagerTests extends BaseTest {
}
