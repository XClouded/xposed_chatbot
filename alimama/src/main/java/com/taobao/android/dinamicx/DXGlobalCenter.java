package com.taobao.android.dinamicx;

import com.taobao.android.dinamicx.bindingx.DXBindingXEventHandler;
import com.taobao.android.dinamicx.expression.ExepressionEvaluation.DXDataParserAbs;
import com.taobao.android.dinamicx.expression.ExepressionEvaluation.DXDataParserAdd;
import com.taobao.android.dinamicx.expression.ExepressionEvaluation.DXDataParserAnd;
import com.taobao.android.dinamicx.expression.ExepressionEvaluation.DXDataParserCeil;
import com.taobao.android.dinamicx.expression.ExepressionEvaluation.DXDataParserColorMap;
import com.taobao.android.dinamicx.expression.ExepressionEvaluation.DXDataParserDXVersionGreaterThanOrEqualTo;
import com.taobao.android.dinamicx.expression.ExepressionEvaluation.DXDataParserDataParserNotFound;
import com.taobao.android.dinamicx.expression.ExepressionEvaluation.DXDataParserDiv;
import com.taobao.android.dinamicx.expression.ExepressionEvaluation.DXDataParserElse;
import com.taobao.android.dinamicx.expression.ExepressionEvaluation.DXDataParserEqual;
import com.taobao.android.dinamicx.expression.ExepressionEvaluation.DXDataParserEventHandlerNotFound;
import com.taobao.android.dinamicx.expression.ExepressionEvaluation.DXDataParserFind;
import com.taobao.android.dinamicx.expression.ExepressionEvaluation.DXDataParserFloor;
import com.taobao.android.dinamicx.expression.ExepressionEvaluation.DXDataParserGet;
import com.taobao.android.dinamicx.expression.ExepressionEvaluation.DXDataParserGreater;
import com.taobao.android.dinamicx.expression.ExepressionEvaluation.DXDataParserGreaterEqual;
import com.taobao.android.dinamicx.expression.ExepressionEvaluation.DXDataParserIf;
import com.taobao.android.dinamicx.expression.ExepressionEvaluation.DXDataParserIndexOf;
import com.taobao.android.dinamicx.expression.ExepressionEvaluation.DXDataParserIsDarkMode;
import com.taobao.android.dinamicx.expression.ExepressionEvaluation.DXDataParserIsRtl;
import com.taobao.android.dinamicx.expression.ExepressionEvaluation.DXDataParserLength;
import com.taobao.android.dinamicx.expression.ExepressionEvaluation.DXDataParserLess;
import com.taobao.android.dinamicx.expression.ExepressionEvaluation.DXDataParserLessEqual;
import com.taobao.android.dinamicx.expression.ExepressionEvaluation.DXDataParserLinearGradient;
import com.taobao.android.dinamicx.expression.ExepressionEvaluation.DXDataParserMod;
import com.taobao.android.dinamicx.expression.ExepressionEvaluation.DXDataParserMul;
import com.taobao.android.dinamicx.expression.ExepressionEvaluation.DXDataParserNot;
import com.taobao.android.dinamicx.expression.ExepressionEvaluation.DXDataParserNotEqual;
import com.taobao.android.dinamicx.expression.ExepressionEvaluation.DXDataParserOr;
import com.taobao.android.dinamicx.expression.ExepressionEvaluation.DXDataParserPlatform;
import com.taobao.android.dinamicx.expression.ExepressionEvaluation.DXDataParserRound;
import com.taobao.android.dinamicx.expression.ExepressionEvaluation.DXDataParserStringConcat;
import com.taobao.android.dinamicx.expression.ExepressionEvaluation.DXDataParserStringLowercase;
import com.taobao.android.dinamicx.expression.ExepressionEvaluation.DXDataParserStringSubstr;
import com.taobao.android.dinamicx.expression.ExepressionEvaluation.DXDataParserStringUppercase;
import com.taobao.android.dinamicx.expression.ExepressionEvaluation.DXDataParserSub;
import com.taobao.android.dinamicx.expression.ExepressionEvaluation.DXDataParserToBindingXUnit;
import com.taobao.android.dinamicx.expression.ExepressionEvaluation.DXDataParserToDouble;
import com.taobao.android.dinamicx.expression.ExepressionEvaluation.DXDataParserToLong;
import com.taobao.android.dinamicx.expression.ExepressionEvaluation.DXDataParserToStr;
import com.taobao.android.dinamicx.expression.ExepressionEvaluation.DXDataParserTrim;
import com.taobao.android.dinamicx.expression.ExepressionEvaluation.DXDataParserTriple;
import com.taobao.android.dinamicx.expression.parser.DXConstantParser;
import com.taobao.android.dinamicx.expression.parser.DXExpressionParser;
import com.taobao.android.dinamicx.expression.parser.DXSubDataParser;
import com.taobao.android.dinamicx.expression.parser.IDXDataParser;
import com.taobao.android.dinamicx.model.DXLongSparseArray;
import com.taobao.android.dinamicx.template.download.IDXDownloader;
import com.taobao.android.dinamicx.template.loader.binary.DXHashConstant;
import com.taobao.android.dinamicx.thread.DXRunnableManager;
import com.taobao.android.dinamicx.view.DXNativeFrameLayout;
import com.taobao.android.dinamicx.view.DXNativeLinearLayout;
import com.taobao.android.dinamicx.view.DXNativeTextView;
import com.taobao.android.dinamicx.widget.DXAdaptiveLinearLayoutWidgetNode;
import com.taobao.android.dinamicx.widget.DXCheckBoxWidgetNode;
import com.taobao.android.dinamicx.widget.DXCountDownTimerWidgetNode;
import com.taobao.android.dinamicx.widget.DXFastTextWidgetNode;
import com.taobao.android.dinamicx.widget.DXFrameLayoutWidgetNode;
import com.taobao.android.dinamicx.widget.DXGridLayoutWidgetNode;
import com.taobao.android.dinamicx.widget.DXImageWidgetNode;
import com.taobao.android.dinamicx.widget.DXLinearLayoutWidgetNode;
import com.taobao.android.dinamicx.widget.DXListLayout;
import com.taobao.android.dinamicx.widget.DXPageIndicator;
import com.taobao.android.dinamicx.widget.DXScrollerIndicator;
import com.taobao.android.dinamicx.widget.DXScrollerLayout;
import com.taobao.android.dinamicx.widget.DXSliderLayout;
import com.taobao.android.dinamicx.widget.DXSwitchWidgetNode;
import com.taobao.android.dinamicx.widget.DXTextInputWidgetNode;
import com.taobao.android.dinamicx.widget.DXTextViewWidgetNode;
import com.taobao.android.dinamicx.widget.DXWidgetNode;
import com.taobao.android.dinamicx.widget.IDXBuilderWidgetNode;
import com.taobao.android.dinamicx.widget.IDXWebImageInterface;

public final class DXGlobalCenter {
    static IDXDownloader dxDownloader;
    static IDXWebImageInterface dxWebImageInterface;
    static DXLongSparseArray<IDXEventHandler> globalEventHandlerMap = new DXLongSparseArray<>();
    static DXLongSparseArray<IDXDataParser> globalParserMap = new DXLongSparseArray<>();
    static DXLongSparseArray<IDXBuilderWidgetNode> globalWidgetNodeMap = new DXLongSparseArray<>();

    static {
        DXRunnableManager.runOnWorkThread(new Runnable() {
            public void run() {
                try {
                    new DXWidgetNode();
                    new DXFrameLayoutWidgetNode();
                    new DXLinearLayoutWidgetNode();
                    new DXSwitchWidgetNode();
                    new DXTextViewWidgetNode();
                    new DXCountDownTimerWidgetNode();
                    new DXListLayout();
                    new DXImageWidgetNode();
                    new DXNativeTextView(DinamicXEngine.getApplicationContext());
                    new DXNativeFrameLayout(DinamicXEngine.getApplicationContext());
                    new DXNativeLinearLayout(DinamicXEngine.getApplicationContext());
                    new DXScrollerLayout();
                    new DXSliderLayout();
                } catch (Throwable unused) {
                }
            }
        });
        try {
            globalParserMap.put(DXHashConstant.DX_DATAPARSER_DATA, new DXExpressionParser());
            globalParserMap.put(DXHashConstant.DX_DATAPARSER_CONST, new DXConstantParser());
            globalParserMap.put(DXHashConstant.DX_DATAPARSER_SUBDATA, new DXSubDataParser());
            globalParserMap.put(DXHashConstant.DX_DATAPARSER_SUBSTR, new DXDataParserStringSubstr());
            globalParserMap.put(DXHashConstant.DX_DATAPARSER_LOWERCASE, new DXDataParserStringLowercase());
            globalParserMap.put(DXHashConstant.DX_DATAPARSER_UPPERCASE, new DXDataParserStringUppercase());
            globalParserMap.put(DXHashConstant.DX_DATAPARSER_CONCAT, new DXDataParserStringConcat());
            globalParserMap.put(DXHashConstant.DX_DATAPARSER_TRIM, new DXDataParserTrim());
            globalParserMap.put(DXHashConstant.DX_DATAPARSER_LEN, new DXDataParserLength());
            globalParserMap.put(DXHashConstant.DX_DATAPARSER_EQUAL, new DXDataParserEqual());
            globalParserMap.put(DXHashConstant.DX_DATAPARSER_AND, new DXDataParserAnd());
            globalParserMap.put(DXHashConstant.DX_DATAPARSER_OR, new DXDataParserOr());
            globalParserMap.put(DXHashConstant.DX_DATAPARSER_NOT, new DXDataParserNot());
            globalParserMap.put(DXHashConstant.DX_DATAPARSER_TRIPLE, new DXDataParserTriple());
            globalParserMap.put(DXHashConstant.DX_DATAPARSER_IF, new DXDataParserIf());
            globalParserMap.put(DXHashConstant.DX_DATAPARSER_ELSE, new DXDataParserElse());
            globalParserMap.put(DXHashConstant.DX_DATAPARSER_ARRAY_GET, new DXDataParserGet());
            globalParserMap.put(DXHashConstant.DX_DATAPARSER_DICT_GET, new DXDataParserGet());
            globalParserMap.put(DXHashConstant.DX_DATAPARSER_ARRAY_FIND, new DXDataParserFind());
            globalParserMap.put(DXHashConstant.DX_DATAPARSER_INDEX_OF, new DXDataParserIndexOf());
            globalParserMap.put(DXHashConstant.DX_DATAPARSER_ISRTL, new DXDataParserIsRtl());
            globalParserMap.put(DXHashConstant.DX_DATAPARSER_PLATFORM, new DXDataParserPlatform());
            globalParserMap.put(DXDataParserAdd.DX_PARSER_ADD, new DXDataParserAdd());
            globalParserMap.put(DXDataParserSub.DX_PARSER_SUB, new DXDataParserSub());
            globalParserMap.put(DXDataParserMul.DX_PARSER_MUL, new DXDataParserMul());
            globalParserMap.put(DXDataParserDiv.DX_PARSER_DIV, new DXDataParserDiv());
            globalParserMap.put(DXDataParserMod.DX_PARSER_MOD, new DXDataParserMod());
            globalParserMap.put(DXDataParserGreater.DX_PARSER_GREATER, new DXDataParserGreater());
            globalParserMap.put(DXDataParserGreaterEqual.DX_PARSER_GREATEREQUAL, new DXDataParserGreaterEqual());
            globalParserMap.put(DXDataParserLess.DX_PARSER_LESS, new DXDataParserLess());
            globalParserMap.put(DXDataParserLessEqual.DX_PARSER_LESSEQUAL, new DXDataParserLessEqual());
            globalParserMap.put(DXDataParserNotEqual.DX_PARSER_NOTEQUAL, new DXDataParserNotEqual());
            globalParserMap.put(DXDataParserToDouble.DX_PARSER_TODOUBLE, new DXDataParserToDouble());
            globalParserMap.put(DXDataParserToLong.DX_PARSER_TOLONG, new DXDataParserToLong());
            globalParserMap.put(DXDataParserToStr.DX_PARSER_TOSTR, new DXDataParserToStr());
            globalParserMap.put(DXDataParserCeil.DX_PARSER_CEIL, new DXDataParserCeil());
            globalParserMap.put(DXDataParserFloor.DX_PARSER_FLOOR, new DXDataParserFloor());
            globalParserMap.put(DXDataParserRound.DX_PARSER_ROUND, new DXDataParserRound());
            globalParserMap.put(DXDataParserAbs.DX_PARSER_ABS, new DXDataParserAbs());
            globalParserMap.put(DXDataParserLinearGradient.DX_PARSER_LINEAR_GRADIENT, new DXDataParserLinearGradient());
            globalParserMap.put(DXDataParserToBindingXUnit.DX_PARSER_TOBINDINGXUNIT, new DXDataParserToBindingXUnit());
            globalParserMap.put(DXDataParserIsDarkMode.DX_PARSER_ISDARKMODE, new DXDataParserIsDarkMode());
            globalParserMap.put(DXDataParserColorMap.DX_PARSER_COLORMAP, new DXDataParserColorMap());
            globalParserMap.put(DXDataParserDataParserNotFound.DX_PARSER_DATAPARSERNOTFOUND, new DXDataParserDataParserNotFound());
            globalParserMap.put(DXDataParserEventHandlerNotFound.DX_PARSER_EVENTHANDLERNOTFOUND, new DXDataParserEventHandlerNotFound());
            globalParserMap.put(DXDataParserDXVersionGreaterThanOrEqualTo.DX_PARSER_DXVERSION_GREATERTHANOREQUALTO, new DXDataParserDXVersionGreaterThanOrEqualTo());
            globalWidgetNodeMap.put(DXHashConstant.DX_WIDGET_VIEW, new DXWidgetNode.Builder());
            globalWidgetNodeMap.put(DXHashConstant.DX_WIDGET_SWITCH, new DXSwitchWidgetNode.Builder());
            globalWidgetNodeMap.put(DXHashConstant.DX_WIDGET_CHECKBOX, new DXCheckBoxWidgetNode.Builder());
            globalWidgetNodeMap.put(DXHashConstant.DX_WIDGET_TEXTINPUT, new DXTextInputWidgetNode.Builder());
            globalWidgetNodeMap.put(DXHashConstant.DX_WIDGET_TEXTVIEW, new DXTextViewWidgetNode.Builder());
            globalWidgetNodeMap.put(DXHashConstant.DX_WIDGET_COUNTDOWNVIEW, new DXCountDownTimerWidgetNode.Builder());
            globalWidgetNodeMap.put(DXHashConstant.DX_WIDGET_IMAGEVIEW, new DXImageWidgetNode.Builder());
            globalWidgetNodeMap.put(DXHashConstant.DX_WIDGET_FASTTEXTVIEW, new DXFastTextWidgetNode.Builder());
            globalWidgetNodeMap.put(DXHashConstant.DX_WIDGET_FRAMELAYOUT, new DXFrameLayoutWidgetNode.Builder());
            globalWidgetNodeMap.put(DXHashConstant.DX_WIDGET_LINEARLAYOUT, new DXLinearLayoutWidgetNode.Builder());
            globalWidgetNodeMap.put(DXHashConstant.DX_WIDGET_LISTLAYOUT, new DXListLayout.Builder());
            globalWidgetNodeMap.put(DXHashConstant.DX_ADAPTIVELINEARLAYOUT, new DXAdaptiveLinearLayoutWidgetNode.Builder());
            globalWidgetNodeMap.put(DXScrollerLayout.DX_SCROLLER_LAYOUT, new DXScrollerLayout.Builder());
            globalWidgetNodeMap.put(DXSliderLayout.DX_SLIDER_LAYOUT, new DXSliderLayout.Builder());
            globalWidgetNodeMap.put(DXPageIndicator.DX_PAGE_INDICATOR, new DXPageIndicator.Builder());
            globalWidgetNodeMap.put(DXScrollerIndicator.DX_SCROLLER_INDICATOR, new DXScrollerIndicator.Builder());
            globalWidgetNodeMap.put(DXGridLayoutWidgetNode.DX_GRID_LAYOUT, new DXGridLayoutWidgetNode.Builder());
            globalEventHandlerMap.put(DXBindingXEventHandler.DX_EVENT_BINDINGX, new DXBindingXEventHandler());
        } catch (Throwable unused) {
        }
    }

    public static DXLongSparseArray<IDXDataParser> getGlobalParserMap() {
        return globalParserMap;
    }

    public static DXLongSparseArray<IDXEventHandler> getGlobalEventHandlerMap() {
        return globalEventHandlerMap;
    }

    public static DXLongSparseArray<IDXBuilderWidgetNode> getGlobalWidgetNodeMap() {
        return globalWidgetNodeMap;
    }

    public static IDXDownloader getDxDownloader() {
        return dxDownloader;
    }

    public static IDXWebImageInterface getDxWebImageInterface() {
        return dxWebImageInterface;
    }
}
