package org.codehaus.groovy.gjit.asm

import org.objectweb.asm.tree.MethodNode

import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.Opcodes
import org.objectweb.asm.tree.ClassNode
import org.objectweb.asm.util.AbstractVisitor
import org.objectweb.asm.commons.EmptyVisitor

พฤติกรรม "ตัววิเคราะห์ def-use แบบบางส่วน"

่ก่อน "การรัน ให้โหลดคลาส Subject เก็บไว้ในตัวแปร cn" {
    def cr = new ClassReader("org.codehaus.groovy.gjit.soot.Subject")
    cn = new ClassNode()
    cr.accept cn, 0
}

ก่อน  "การรัน เตรียมเมธอด two, ตัวคำสั่ง aload สำหรับใช้ทดสอบ และ invoke สำหรับใช้วัดผล" {
    two = cn.@methods.find { it.name == "two" }
    def ins = two.instructions
    aload  = ins.get(5)
    invoke = ins.get(15)
}

ก่อน  "การรัน เตรียมวัตถุสำหรับทดสอบ p ด้วย aload และค้นหาไปถึง invokeinterface" {
	p = new PartialDefUseAnalyser(two, aload, Opcodes.INVOKEINTERFACE)
}

วัตถุ "ควรทำการวิเคราะห์ def-use แล้วคืนค่าเป็นตัวคำสั่งเดียวกับ invoke" {
	def result = p.analyse0()
	result.should == invoke
}