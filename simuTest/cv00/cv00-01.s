	.= 0x0100	; Program: start address
	JMP __START	; Program: jump to __START
__START:			; Program: start label
	MOV #0x1000, SP	; Program: initializing SP
	MOV #2, (SP)+	; Number: push Number
	MOV #3, (SP)+	; Number: push Number
	MOV -(SP), R0 	; ExpressionAdd: pop right value to R0[int]
	MOV -(SP), R1 	; ExpressionAdd: pop left value to R1[int]
	ADD R1, R0	; ExpressionAdd: add R1[int] to R0[int]
	MOV R0, (SP)+	; ExpressionAdd: push result R0[int]
	MOV #3, (SP)+	; Number: push Number
	MOV -(SP), R0 	; ExpressionAdd: pop right value to R0[int]
	MOV -(SP), R1 	; ExpressionAdd: pop left value to R1[int]
	ADD R1, R0	; ExpressionAdd: add R1[int] to R0[int]
	MOV R0, (SP)+	; ExpressionAdd: push result R0[int]
	MOV -(SP), R0 	; Program: Move the calculation result to R0 (for checking the result).
	HLT		; Program: HALT
	.end		; Program: 
