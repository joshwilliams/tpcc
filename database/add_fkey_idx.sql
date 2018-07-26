CREATE INDEX table_type ON default (type);

CREATE INDEX idx_customer ON default (c_w_id,c_d_id,c_last,c_first) WHERE type = "customer" WITH {"defer_build":true};
CREATE INDEX idx_orders ON default (o_w_id,o_d_id,o_c_id,o_id) WHERE type = "orders" WITH {"defer_build":true};
CREATE INDEX fkey_stock_2 ON default (s_i_id) WHERE type = "stock" WITH {"defer_build":true};
CREATE INDEX fkey_order_line_2 ON default (ol_supply_w_id,ol_i_id) WHERE type = "order_line" WITH {"defer_build":true};

CREATE INDEX customer_pk ON default (c_w_id, c_d_id, c_id) WHERE type = "customer" WITH {"defer_build":true};
CREATE INDEX new_orders_pk ON default (no_w_id, no_d_id, no_o_id) WHERE type = "new_orders" WITH {"defer_build":true};
CREATE INDEX orders_pk ON default (o_w_id, o_d_id, o_id) WHERE type = "orders" WITH {"defer_build":true};
CREATE INDEX order_line_pk ON default (ol_w_id, ol_d_id, ol_o_id, ol_number) WITH {"defer_build":true};
CREATE INDEX item_pk ON default (i_id) WITH {"defer_build":true};
CREATE INDEX stock_pk ON default (s_w_id, s_i_id) WITH {"defer_build":true};

BUILD INDEX ON default(customer_pk, fkey_order_line_2, fkey_stock_2, idx_customer, idx_orders, item_pk, new_orders_pk, order_line_pk, orders_pk, stock_pk) USING GSI;
