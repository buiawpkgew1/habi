package net.fabricmc.example.Command;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;

import java.util.List;

public interface ISchematic {
    /**
     * @param x 相对于原点的块的x位置
     * @param y 相对于原点的块的y位置
     * @param z 相对于原点的块的z位置
     */
    default boolean inSchematic(int x, int y, int z, IBlockState currentState){
        return x>=0&&x<widthX()&&y >= 0 && y < heightY() && z >= 0 && z < lengthZ();
    }
    default int size(EnumFacing.Axis axis){
        switch (axis){
            case X:
                return widthX();
                case Y:
                    return heightY();
                    case Z:
                        return lengthZ();
            default:
                throw new UnsupportedOperationException(axis+"");
        }
    }

    IBlockState desiredState(int x, int y, int z, IBlockState current, List<IBlockState> approxPlaceable);

    default void reset() {}
    int widthX();
    int heightY();
    int lengthZ();

}