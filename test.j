.source test.j
.class public abstract net/virtualvoid/functional/Runner
.super java/lang/Object
.implements net/virtualvoid/functional/Functions$Function0
.method public <init>()V
 aload 0
 invokespecial java/lang/Object/<init>()V 
 return
.end method
.method public apply()Ljava/lang/Object;
.limit locals 3
.limit stack 5
 aload 0
 invokevirtual net/virtualvoid/functional/Runner/is()[I
 dup
 astore 1
 arraylength
 iconst_1
 isub
 dup
 istore 2
 iflt l1
l2:
 aload 1
 iload 2
 iaload
 iconst_2
 irem
 ifne l3
 aload 1
 iload 2
 iconst_m1
 iastore
l3:
 iinc 2 -1
 iload 2
 ifge l2
l1: 
 aconst_null
 areturn
.end method
.method public abstract is()[I
.end method
.method public test(Ljava/lang/Integer;)V
.limit locals 2
.limit stack 2
 aload 1
 bipush 28
 putfield java/lang/Integer/value I
 return
.end method
