#define MEMORY_SIZE(26)

//变量类型
typedef enum {
	var_null=0,
	var_double,
	var_string		
}variant_type;


typedef char STRING[128];

typedef struct
{
	variant_type type;
	//定义联合 共享内存
	union {
		double i;
		STRING s;
	};

}VARIANT;
extern VARIANT memory[MEMORY_SIZE];
typedef VARIANT OPERAND;