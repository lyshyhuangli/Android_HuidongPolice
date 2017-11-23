package com.huizhou.huidongpolice.afterLogin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.huizhou.huidongpolice.R;
import com.huizhou.huidongpolice.Request.GetJingQingInfoByIdReq;
import com.huizhou.huidongpolice.database.vo.JingQingInfoRecord;
import com.huizhou.huidongpolice.utils.CommonCache;
import com.huizhou.huidongpolice.utils.CommonUtils;
import com.huizhou.huidongpolice.utils.Flags;
import com.huizhou.huidongpolice.utils.Log4J;
import com.huizhou.huidongpolice.utils.ProcessDataBaseThread;
import com.huizhou.huidongpolice.utils.StaticNumber;

/**
 * 警讯详情界面
 */
public class ActivityJingInfoDetail extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jing_info_detail);

        SharedPreferences userSettings = this.getSharedPreferences("userInfo", 0);
        String userName = userSettings.getString("loginUserName", "default");
        String classInfo = CommonUtils.getFileLineMethod(new Exception());

        //获取信息编号
        Intent it = getIntent();
        String jinQingId = it.getStringExtra("jinQingId");

        ProcessDataBaseThread pd = new ProcessDataBaseThread();
        pd.setFlag(Flags.ONE_ZERO_ZERO_THREE);

        GetJingQingInfoByIdReq req = new GetJingQingInfoByIdReq();
        req.setId(jinQingId);
        req.setOperatorId(userName);
        pd.setObject(req);

        Thread t = new Thread(pd);
        t.start();
        JingQingInfoRecord jingQing = null;
        try
        {
            for (int i = 0; i <= StaticNumber.TWO_ZERO; i++)
            {
                Thread.sleep(StaticNumber.FIVE_HUNDRED);

                Object o = CommonCache.dataCache.get(Flags.ONE_ZERO_ZERO_THREE);
                if (null != o)
                {
                    jingQing = (JingQingInfoRecord) o;
                    CommonCache.dataCache.remove(Flags.ONE_ZERO_ZERO_THREE);
                    break;
                }
                else
                {
                    if (i == StaticNumber.TEN)
                    {
                        Thread t2 = new Thread(pd);
                        t2.start();

                        Thread.sleep(StaticNumber.FIVE_HUNDRED);
                        o = CommonCache.dataCache.get(Flags.ONE_ZERO_ZERO_THREE);
                        if (null != o)
                        {
                            jingQing = (JingQingInfoRecord) o;
                            CommonCache.dataCache.remove(Flags.ONE_ZERO_ZERO_THREE);
                            break;
                        }
                    }
                }
            }
        }
        catch (Exception e)
        {
            Log4J.getLogger(this.getClass()).error("查询警讯失败：" + e.getMessage());
            CommonUtils.sendLogsToServer(classInfo, "查询警讯失败：" + e.getMessage(), userName);
        }

        if (null != jingQing)
        {
            TextView idNo = (TextView) findViewById(R.id.idNo);
            idNo.setText(jingQing.getIdNo());
            TextView comformXingZhi = (TextView) findViewById(R.id.comformXingZhi);
            comformXingZhi.setText(jingQing.getComformXingZhi());
            TextView place = (TextView) findViewById(R.id.place);
            place.setText(jingQing.getPlace());
            TextView xingZhi = (TextView) findViewById(R.id.xingZhi);
            xingZhi.setText(jingQing.getXingZhi());
            TextView briefly = (TextView) findViewById(R.id.briefly);
            briefly.setText(jingQing.getBriefly());
            TextView date1 = (TextView) findViewById(R.id.date1);
            date1.setText(jingQing.getDate1());
            TextView date2 = (TextView) findViewById(R.id.date2);
            date2.setText(jingQing.getDate2());
            TextView placeDetail = (TextView) findViewById(R.id.placeDetail);
            placeDetail.setText(jingQing.getPlaceDetail());
            TextView alarmMode = (TextView) findViewById(R.id.alarmMode);
            alarmMode.setText(jingQing.getAlarmMode());
            TextView alarmCompany = (TextView) findViewById(R.id.alarmCompany);
            alarmCompany.setText(jingQing.getAlarmCompany());
            TextView process = (TextView) findViewById(R.id.process);
            process.setText(jingQing.getProcess());
            TextView numberCaptured = (TextView) findViewById(R.id.numberCaptured);
            numberCaptured.setText(jingQing.getNumberCaptured());
            TextView rescuedPopulation = (TextView) findViewById(R.id.rescuedPopulation);
            rescuedPopulation.setText(jingQing.getRescuedPopulation());
            TextView rescuedChildPopulation = (TextView) findViewById(R.id.rescuedChildPopulation);
            rescuedChildPopulation.setText(jingQing.getRescuedChildPopulation());
            TextView chuJingPerson = (TextView) findViewById(R.id.chuJingPerson);
            chuJingPerson.setText(jingQing.getChuJingPerson());
            TextView jingLi = (TextView) findViewById(R.id.jingLi);
            jingLi.setText(jingQing.getJingLi());
            TextView chuanCi = (TextView) findViewById(R.id.chuanCi);
            chuanCi.setText(jingQing.getChuanCi());
            TextView hangkongNumber = (TextView) findViewById(R.id.hangkongNumber);
            hangkongNumber.setText(jingQing.getHangkongNumber());
            TextView processResult = (TextView) findViewById(R.id.processResult);
            processResult.setText(jingQing.getProcessResult());
            TextView leaderOpinion = (TextView) findViewById(R.id.leaderOpinion);
            leaderOpinion.setText(jingQing.getLeaderOpinion());
            TextView fillFormDate = (TextView) findViewById(R.id.fillFormDate);
            fillFormDate.setText(jingQing.getFillFormDate());
            TextView jieJingPerson = (TextView) findViewById(R.id.jieJingPerson);
            jieJingPerson.setText(jingQing.getJieJingPerson());
            TextView alarmDate = (TextView) findViewById(R.id.alarmDate);
            alarmDate.setText(jingQing.getAlarmDate());
            TextView placeArea = (TextView) findViewById(R.id.placeArea);
            placeArea.setText(jingQing.getPlaceArea());
            TextView placeStreet = (TextView) findViewById(R.id.placeStreet);
            placeStreet.setText(jingQing.getPlaceStreet());
            TextView jingQingType = (TextView) findViewById(R.id.jingQingType);
            jingQingType.setText(jingQing.getJingQingType());
            TextView isValid = (TextView) findViewById(R.id.isValid);
            isValid.setText(jingQing.getIsValid());
            TextView discoverTime = (TextView) findViewById(R.id.discoverTime);
            discoverTime.setText(jingQing.getDiscoverTime());
            TextView subordinateCommunity = (TextView) findViewById(R.id.subordinateCommunity);
            subordinateCommunity.setText(jingQing.getSubordinateCommunity());
            TextView busNumber = (TextView) findViewById(R.id.busNumber);
            busNumber.setText(jingQing.getBusNumber());
            TextView coordinateX = (TextView) findViewById(R.id.coordinateX);
            coordinateX.setText(jingQing.getCoordinateX());
            TextView coordinateY = (TextView) findViewById(R.id.coordinateY);
            coordinateY.setText(jingQing.getCoordinateY());
            TextView sunshiZhekuan = (TextView) findViewById(R.id.sunshiZhekuan);
            sunshiZhekuan.setText(jingQing.getSunshiZhekuan());
            TextView injuredNumber = (TextView) findViewById(R.id.injuredNumber);
            injuredNumber.setText(jingQing.getInjuredNumber());
            TextView isDelete = (TextView) findViewById(R.id.isDelete);
            isDelete.setText(jingQing.getIsDelete());
            TextView remark = (TextView) findViewById(R.id.remark);
            remark.setText(jingQing.getRemark());
            TextView shizhuPhone = (TextView) findViewById(R.id.shizhuPhone);
            shizhuPhone.setText(jingQing.getShizhuPhone());
            TextView informantPhone = (TextView) findViewById(R.id.informantPhone);
            informantPhone.setText(jingQing.getInformantPhone());
            TextView district = (TextView) findViewById(R.id.district);
            district.setText(jingQing.getDistrict());
            TextView processTime = (TextView) findViewById(R.id.processTime);
            processTime.setText(jingQing.getProcessTime());
            TextView deadNumber = (TextView) findViewById(R.id.deadNumber);
            deadNumber.setText(jingQing.getDeadNumber());
            TextView policeArea = (TextView) findViewById(R.id.policeArea);
            policeArea.setText(jingQing.getPoliceArea());
            TextView fillFormPerson = (TextView) findViewById(R.id.fillFormPerson);
            fillFormPerson.setText(jingQing.getFillFormPerson());
        }

    }

    public void tobackJingqing(View view)
    {
        super.onBackPressed();
    }
}
