package org.droidbaza.logme

import org.jetbrains.kotlin.backend.common.extensions.IrGenerationExtension
import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.backend.common.lower.DeclarationIrBuilder
import org.jetbrains.kotlin.ir.IrStatement
import org.jetbrains.kotlin.ir.builders.irCall
import org.jetbrains.kotlin.ir.builders.irConcat
import org.jetbrains.kotlin.ir.builders.irGet
import org.jetbrains.kotlin.ir.builders.irString
import org.jetbrains.kotlin.ir.declarations.IrFunction
import org.jetbrains.kotlin.ir.declarations.IrModuleFragment
import org.jetbrains.kotlin.ir.expressions.IrConst
import org.jetbrains.kotlin.ir.expressions.addArgument
import org.jetbrains.kotlin.ir.expressions.impl.IrBlockBodyImpl
import org.jetbrains.kotlin.ir.symbols.IrSimpleFunctionSymbol
import org.jetbrains.kotlin.ir.types.classFqName
import org.jetbrains.kotlin.ir.util.file
import org.jetbrains.kotlin.ir.util.parentClassOrNull
import org.jetbrains.kotlin.ir.visitors.IrElementTransformerVoid
import org.jetbrains.kotlin.ir.visitors.transformChildrenVoid
import org.jetbrains.kotlin.name.CallableId
import org.jetbrains.kotlin.name.FqName
import org.jetbrains.kotlin.name.Name

class LogMeExtension : IrGenerationExtension {

    private var printSymbol: IrSimpleFunctionSymbol? = null

    override fun generate(moduleFragment: IrModuleFragment, pluginContext: IrPluginContext) {
        moduleFragment.transformChildrenVoid(object : IrElementTransformerVoid() {
            override fun visitFunction(declaration: IrFunction): IrStatement {
                val annotation =
                    declaration.annotations.find { it.type.classFqName?.asString()?.endsWith(".LogMe") == true }
                if (annotation != null) {
                    val className = declaration.parentClassOrNull?.name?.asString() ?: "TopLevel"
                    val functionName = declaration.name.asString()
                    val msg = (annotation.getValueArgument(0) as? IrConst)?.value?.toString()
                    val tag = (annotation.getValueArgument(1) as? IrConst)?.value?.toString() ?: "LOG_ME"
                    val showArgs = (annotation.getValueArgument(2) as? IrConst)?.value as? Boolean ?: true
                    val irBuilder = DeclarationIrBuilder(pluginContext, declaration.symbol)

                    if (printSymbol == null) {
                        val callableId = CallableId(FqName("kotlin.io"), Name.identifier("println"))
                        printSymbol = pluginContext.referenceFunctions(callableId)
                            .firstOrNull()
                    }
                    val fileEntry = declaration.file.fileEntry
                    val fileName = fileEntry.name.substringAfterLast("/")
                    val lineNumber = fileEntry.getLineNumber(declaration.startOffset) + 1
                    val sourceRef = "($fileName:$lineNumber)"

                    val message = irBuilder.irConcat().apply {
                        val argMsg = "$tag:$sourceRef $className.$functionName ${msg ?: ""} "
                        addArgument(irBuilder.irString(argMsg))
                        if (showArgs) {
                            declaration.valueParameters.forEachIndexed { index, param ->
                                if (index > 0) addArgument(irBuilder.irString(", "))
                                addArgument(irBuilder.irString("${param.name.asString()}="))
                                addArgument(irBuilder.irGet(param.type, param.symbol))
                            }
                        }
                        addArgument(irBuilder.irString(")"))
                    }
                    val logStatement = printSymbol?.let {
                        irBuilder.irCall(it).apply {
                            putValueArgument(0, message)
                        }
                    }
                    val body = (declaration.body as? IrBlockBodyImpl) ?: return super.visitFunction(declaration)
                    logStatement?.let {
                        body.statements.add(0, logStatement)
                    }
                }
                return super.visitFunction(declaration)
            }
        })
    }
}
