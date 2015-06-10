// in expression.c
static const OPERATOR operators[] = {
    /* 算数运算 */
    {2, 17, 1, left2right, oper_lparen},     // 左括号
    {2, 17, 17, left2right, oper_rparen},    // 右括号
    {2, 12, 12, left2right, oper_plus},      // 加
    {2, 12, 12, left2right, oper_minus},     // 减
    {2, 13, 13, left2right, oper_multiply},  // 乘
    {2, 13, 13, left2right, oper_divide},    // 除
    {2, 13, 13, left2right, oper_mod},       // 模
    {2, 14, 14, left2right, oper_power},     // 幂
    {1, 16, 15, right2left, oper_positive},  // 正号
    {1, 16, 15, right2left, oper_negative},  // 负号
    {1, 16, 15, left2right, oper_factorial}, // 阶乘
    /* 关系运算 */
    {2, 10, 10, left2right, oper_lt},        // 小于
    {2, 10, 10, left2right, oper_gt},        // 大于
    {2, 9, 9, left2right, oper_eq},          // 等于
    {2, 9, 9, left2right, oper_ne},          // 不等于
    {2, 10, 10, left2right, oper_le},        // 不大于
    {2, 10, 10, left2right, oper_ge},        // 不小于
    /* 逻辑运算 */
    {2, 5, 5, left2right, oper_and},         // 且
    {2, 4, 4, left2right, oper_or},          // 或
    {1, 15, 15, right2left, oper_not},       // 非
    /* 赋值 */
    // BASIC 中赋值语句不属于表达式！
    {2, 2, 2, right2left, oper_assignment},  // 赋值
    /* 最小优先级 */
    {2, 0, 0, right2left, oper_min}          // 栈底
};