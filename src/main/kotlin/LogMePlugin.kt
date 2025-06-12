package com.github.droidbaza.logmeplugin

import org.jetbrains.kotlin.backend.common.extensions.FirIncompatiblePluginAPI
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
import org.jetbrains.kotlin.ir.expressions.addArgument
import org.jetbrains.kotlin.ir.expressions.impl.IrBlockBodyImpl
import org.jetbrains.kotlin.ir.symbols.IrSimpleFunctionSymbol
import org.jetbrains.kotlin.ir.symbols.UnsafeDuringIrConstructionAPI
import org.jetbrains.kotlin.ir.types.classFqName
import org.jetbrains.kotlin.ir.util.getAnnotationValueOrNull
import org.jetbrains.kotlin.ir.util.parentClassOrNull
import org.jetbrains.kotlin.ir.visitors.IrElementTransformerVoid
import org.jetbrains.kotlin.ir.visitors.transformChildrenVoid
import org.jetbrains.kotlin.name.FqName

class LogMePlugin : IrGenerationExtension {

    private var printSymbol: IrSimpleFunctionSymbol? = null

    @OptIn(
        FirIncompatiblePluginAPI::class,
        UnsafeDuringIrConstructionAPI::class
    )
    override fun generate(moduleFragment: IrModuleFragment, pluginContext: IrPluginContext) {
        moduleFragment.transformChildrenVoid(object : IrElementTransformerVoid() {
            override fun visitFunction(declaration: IrFunction): IrStatement {
                val annotation =
                    declaration.annotations.find { it.type.classFqName?.asString()?.endsWith(".LogMe") == true }
                if (annotation != null) {
                    val className = declaration.parentClassOrNull?.name?.asString() ?: "TopLevel"
                    val functionName = declaration.name.asString()
                    val msg = annotation.getAnnotationValueOrNull<String>("msg") ?: ""
                    val tag = annotation.getAnnotationValueOrNull<String>("tag") ?: "LOG_ME"
                    val showArgs = annotation.getAnnotationValueOrNull<Boolean>("showArgs") ?: true
                    val irBuilder = DeclarationIrBuilder(pluginContext, declaration.symbol)

                    if (printSymbol == null) {
                        printSymbol = pluginContext.referenceFunctions(FqName("kotlin.io.println"))
                            .find { it.owner.valueParameters.size == 1 }
                    }

                    val message = irBuilder.irConcat().apply {
                        val argMsg = "$tag: Called $className.$functionName ${msg ?: ""}"
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
