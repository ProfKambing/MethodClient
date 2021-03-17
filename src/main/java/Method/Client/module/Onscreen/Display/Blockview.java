package Method.Client.module.Onscreen.Display;

import Method.Client.module.Category;
import Method.Client.module.Module;
import Method.Client.module.Onscreen.PinableFrame;
import Method.Client.managers.Setting;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.client.event.RenderGameOverlayEvent.Text;
import org.lwjgl.input.Keyboard;


import static Method.Client.Main.setmgr;
import static net.minecraft.client.gui.Gui.drawRect;

public final class Blockview extends Module {
    public Blockview() {
        super("Blockview", Keyboard.KEY_NONE, Category.ONSCREEN, "BlockOverlay");
    }

    static Setting xpos;
    static Setting ypos;
    static Setting Background;
    static Setting Shadow;
    static Setting Frame;
    static Setting Color;
    static Setting TextColor;
    static Setting FontSize;
    static Setting Text;
    static Setting Image;

    @Override
    public void setup() {
        this.visible = false;
        setmgr.add(Color = new Setting("BGColor", this, .01, 0, .30, .22));
        setmgr.add(TextColor = new Setting("TextColor", this, .01, 0, 0, .55));
        setmgr.add(Background = new Setting("Background", this, false));
        setmgr.add(Shadow = new Setting("Shadow", this, true));
        setmgr.add(Text = new Setting("Text", this, true));
        setmgr.add(Image = new Setting("Image", this, true));
        setmgr.add(Frame = new Setting("Font", this, "Times", fontoptions()));
        setmgr.add(FontSize = new Setting("FontSize", this, 22, 10, 40, true));
        setmgr.add(xpos = new Setting("xpos", this, 200, -20, (mc.displayWidth ) + 40, true));
        setmgr.add(ypos = new Setting("ypos", this, 10, -20, (mc.displayHeight) + 40, true));
    }

    @Override
    public void onEnable() {
        PinableFrame.Toggle("BlockviewSET", true);
    }

    @Override
    public void onDisable() {
        PinableFrame.Toggle("BlockviewSET", false);
    }

    public static class BlockviewRUN extends PinableFrame {

        public BlockviewRUN() {
            super("BlockviewSET", new String[]{}, (int) ypos.getValDouble(), (int) xpos.getValDouble());
        }

        @Override
        public void setup() {
            GetSetup(this,xpos,ypos,Frame,FontSize);
        }

        @Override
        public void Ongui() {
            GetInit(this, xpos, ypos, Frame,FontSize);

        }

        @Override
        public void onRenderGameOverlay(Text event) {

            if (mc.objectMouseOver.typeOfHit == RayTraceResult.Type.BLOCK) {
                Block block = mc.world.getBlockState(mc.objectMouseOver.getBlockPos()).getBlock();

                if (Block.getIdFromBlock(block) == 0)
                    return;
                if (Text.getValBoolean()) {
                    if (Background.getValBoolean())
                        drawRect(this.x, this.y + 10, this.x + widthcal(Frame, block.getLocalizedName()), this.y + 22, Color.getcolor());
                    fontSelect(Frame, block.getLocalizedName(), this.getX(), this.getY() + 10, TextColor.getcolor(), Shadow.getValBoolean());
                }
                if (Image.getValBoolean()) {
                    GlStateManager.pushMatrix();
                    GlStateManager.translate(this.x+8, this.y-1, 0);
                    GlStateManager.scale(0.75, 0.75, 0.75);

                    RenderHelper.enableGUIStandardItemLighting();
                    mc.getRenderItem().renderItemAndEffectIntoGUI(new ItemStack(block), 0, 0);
                    RenderHelper.disableStandardItemLighting();

                    GlStateManager.popMatrix();
                }
            }
            super.onRenderGameOverlay(event);

        }


    }

}

