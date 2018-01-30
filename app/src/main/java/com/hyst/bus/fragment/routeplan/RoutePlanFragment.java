package com.hyst.bus.fragment.routeplan;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.services.busline.BusStationItem;
import com.amap.api.services.route.BusPath;
import com.amap.api.services.route.BusStep;
import com.hyst.bus.R;
import com.hyst.bus.adapter.RecyclerAdapter;
import com.hyst.bus.fragment.BaseLazyFragment;
import com.hyst.bus.model.RecyclerHolder;
import com.hyst.bus.model.SchemeBusStep;
import com.hyst.bus.util.AMapUtil;
import com.hyst.bus.util.ViewUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/1/18.
 */

public class RoutePlanFragment extends BaseLazyFragment {

    private TextView tv_route;
    private TextView tv_name;
    private TextView tv_address;
    //
    private RecyclerView recyclerView;
    private RecyclerAdapter adapter;
    private List<SchemeBusStep> data;
    private BusPath busPath;
    //
    private View[] mPoint;// 存放小圆点
    private ViewGroup mGroup;// 小圆点选中布局
    private int currenPosition;
    private int size;

    public void setData(BusPath busPath, int currenPosition, int size) {
        this.currenPosition = currenPosition;
        this.busPath = busPath;
        this.size = size;
    }

    @Override
    protected void loadData() {
        if (busPath == null || busPath.getSteps() == null) {
            return;
        }
        SchemeBusStep start = new SchemeBusStep(null);
        start.setStart(true);
        data.add(start);
        for (BusStep busStep : busPath.getSteps()) {
            if (busStep.getWalk() != null && busStep.getWalk().getDistance() > 1) {
                SchemeBusStep walk = new SchemeBusStep(busStep);
                walk.setWalk(true);
                data.add(walk);
            }
            if (busStep.getBusLine() != null) {
                SchemeBusStep bus = new SchemeBusStep(busStep);
                bus.setBus(true);
                data.add(bus);
            }
            if (busStep.getRailway() != null) {
                SchemeBusStep railway = new SchemeBusStep(busStep);
                railway.setRailway(true);
                data.add(railway);
            }
            if (busStep.getTaxi() != null) {
                SchemeBusStep taxi = new SchemeBusStep(busStep);
                taxi.setTaxi(true);
                data.add(taxi);
            }
        }
        SchemeBusStep end = new SchemeBusStep(null);
        end.setEnd(true);
        data.add(end);
        adapter.setDatas(data);
        //
        tv_route.setText("方案" + (currenPosition + 1));
        tv_name.setText(AMapUtil.getBusPathTitle(busPath));
        tv_address.setText(AMapUtil.getBusPathDes(busPath));
        initPoint();
    }

    @Override
    protected int setLayoutId() {
        return R.layout.fragment_route_plan;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        tv_route = (TextView) view.findViewById(R.id.tv_route);
        tv_name = (TextView) view.findViewById(R.id.tv_name);
        mGroup = (ViewGroup) view.findViewById(R.id.view_group);
        tv_address = (TextView) view.findViewById(R.id.tv_address);
    }

    @Override
    protected void initData() {
        data = new ArrayList<>();
        adapter = new RecyclerAdapter<SchemeBusStep>(context, data, R.layout.item_route_detail) {
            @Override
            public void convert(final RecyclerHolder holder, final SchemeBusStep step) {
                holder.getView(R.id.ll_station).setVisibility(View.GONE);
                holder.getView(R.id.v_line_start).setVisibility(View.VISIBLE);
                holder.getView(R.id.v_line_end).setVisibility(View.VISIBLE);
                LinearLayout ll_pass_station = holder.getView(R.id.ll_pass_station);
                ll_pass_station.removeAllViews();
                int position = holder.getAdapterPosition();
                if (position == 0) {
                    holder.setText(R.id.tv_name, "起点");
                    holder.setImageResource(R.id.iv_type, R.drawable.ic_start);
                    holder.getView(R.id.v_line_start).setVisibility(View.INVISIBLE);
                } else if (position == data.size() - 1) {
                    holder.setImageResource(R.id.iv_type, R.drawable.ic_end);
                    holder.getView(R.id.v_line_end).setVisibility(View.INVISIBLE);
                    holder.setText(R.id.tv_name, "终点");
                } else {
                    if (step.isWalk() && step.getWalk() != null && step.getWalk().getDistance() > 0) {
                        //步行路线
                        holder.setImageResource(R.id.iv_type, R.drawable.ic_man);
                        holder.setText(R.id.tv_name, "步行" + (int) step.getWalk().getDistance() + "米");
                    } else if (step.isBus() && step.getBusLines().size() > 0) {
                        //公交路线
                        holder.setImageResource(R.id.iv_type, R.drawable.ic_bus);
                        holder.setText(R.id.tv_address, (step.getBusLines().get(0).getPassStationNum() + 1) + "站");
                        holder.setText(R.id.tv_name, step.getBusLines().get(0).getBusLineName());
                        List<BusStationItem> passStations = new ArrayList<>();
                        step.getBusLines().get(0).getPassStations();
                        //上车站
                        BusStationItem departureBusStation = step.getBusLines().get(0).getDepartureBusStation();
                        //下车站
                        BusStationItem arrivalBusStation = step.getBusLines().get(0).getArrivalBusStation();
                        passStations.add(departureBusStation);
                        passStations.addAll(step.getBusLines().get(0).getPassStations());
                        passStations.add(arrivalBusStation);
                        for (int i = 0; i < passStations.size(); i++) {
                            TextView textView = new TextView(context);
                            textView.setPadding(0, 10, 0, 10);
                            textView.setText(passStations.get(i).getBusStationName());
                            textView.setTextColor(getResources().getColor(R.color.station_black));
                            ll_pass_station.addView(textView);
                        }
                        holder.getView(R.id.ll_pass_station).setVisibility(View.GONE);
                        holder.getView(R.id.ll_station).setVisibility(View.VISIBLE);
                        holder.setText(R.id.tv_station, "上：" + departureBusStation.getBusStationName() + "\n下：" + arrivalBusStation.getBusStationName());
                    } else if (step.isRailway() && step.getRailway() != null) {
                        //地铁路线
                        holder.setImageResource(R.id.iv_type, R.drawable.ic_bus);
                        holder.getView(R.id.ll_station).setVisibility(View.VISIBLE);
                        holder.setText(R.id.tv_address, step.getRailway().getName());
                        holder.setText(R.id.tv_name, (step.getRailway().getViastops().size() + 1) + "站");
                    } else if (step.isTaxi() && step.getTaxi() != null) {
                        //出租路线
                        holder.setImageResource(R.id.iv_type, R.drawable.ic_bus);
                        holder.setText(R.id.tv_name, "打车到终点");
                    } else {
                        holder.setImageResource(R.id.iv_type, R.drawable.ic_bus);
                    }
                }
                holder.setOnClickListener(R.id.ll_station, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (holder.getView(R.id.ll_pass_station).getVisibility() == View.VISIBLE) {
                            holder.getView(R.id.ll_pass_station).setVisibility(View.GONE);
                            holder.setImageResource(R.id.iv_exchange, R.drawable.layer_right);
                        } else {
                            holder.setImageResource(R.id.iv_exchange, R.drawable.layer_down);
                            holder.getView(R.id.ll_pass_station).setVisibility(View.VISIBLE);
                        }
                    }
                });
            }
        };
        recyclerView = ViewUtil.getVRowsNoLine(context, recyclerView, 1);
        recyclerView.setAdapter(adapter);
    }

    /**
     * 初始化小圆点
     */
    private void initPoint() {
        // 给数组添加小圆点
        mPoint = new View[size];
        LinearLayout.LayoutParams paramsv = new LinearLayout.LayoutParams(20,
                20);
        for (int i = 0; i < mPoint.length; i++) {
            View v = new View(getActivity());
            if (i == currenPosition) {
                // 设置大小
                v.setLayoutParams(paramsv);
                // 设置背景
                v.setBackgroundResource(R.drawable.ic_green_round);
                paramsv.setMargins(6, 0, 6, 0);
                mPoint[i] = v;
                mGroup.addView(v);
            } else {
                // 设置大小
                v.setLayoutParams(paramsv);
                // 设置背景
                v.setBackgroundResource(R.drawable.yuan_toming);
                paramsv.setMargins(6, 0, 6, 0);
                mPoint[i] = v;
                mGroup.addView(v);
            }
        }
    }

}
